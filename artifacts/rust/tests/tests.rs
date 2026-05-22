use std::error::Error;

use my_space_music_catalogue::statements;
use testcontainers::runners::AsyncRunner as _;

struct SharedTestContext {
    pool: deadpool_postgres::Pool,
    _container: testcontainers::ContainerAsync<testcontainers_modules::postgres::Postgres>,
}

static SHARED_TEST_CONTEXT: tokio::sync::OnceCell<std::sync::Mutex<Option<SharedTestContext>>> =
    tokio::sync::OnceCell::const_new();

#[dtor::dtor(unsafe, method = at_module_exit)]
fn cleanup_shared_test_context() {
    if let Some(context) = SHARED_TEST_CONTEXT.get() {
        if let Some(context) = context.lock().unwrap().take() {
            std::thread::spawn(move || {
                let runtime = tokio::runtime::Builder::new_current_thread()
                    .enable_all()
                    .build()
                    .expect("Failed to build cleanup runtime");

                runtime.block_on(async move {
                    drop(context);
                });
            })
            .join()
            .expect("Failed to join cleanup thread");
        }
    }
}

async fn setup_pool() -> std::sync::Mutex<Option<SharedTestContext>> {
    let container = testcontainers_modules::postgres::Postgres::default()
        .start()
        .await
        .expect("Failed to start Postgres container");

    let host_port = container
        .get_host_port_ipv4(5432)
        .await
        .expect("Failed to get host port");

    let mut cfg = deadpool_postgres::Config::new();
    cfg.manager = Some(deadpool_postgres::ManagerConfig {
        recycling_method: deadpool_postgres::RecyclingMethod::Verified,
    });
    cfg.host = Some("127.0.0.1".to_string());
    cfg.port = Some(host_port);
    cfg.user = Some("postgres".to_string());
    cfg.password = Some("postgres".to_string());
    cfg.dbname = Some("postgres".to_string());

    let pool = cfg
        .create_pool(
            Some(deadpool_postgres::Runtime::Tokio1),
            tokio_postgres::NoTls,
        )
        .expect("Failed to create pool");

    apply_migrations(host_port).await;

    std::sync::Mutex::new(Some(SharedTestContext {
        pool,
        _container: container,
    }))
}

async fn shared_pool() -> deadpool_postgres::Pool {
    SHARED_TEST_CONTEXT
        .get_or_init(setup_pool)
        .await
        .lock()
        .unwrap()
        .as_ref()
        .expect("Shared test context should be initialized")
        .pool
        .clone()
}

async fn apply_migrations(host_port: u16) {
    const MIGRATIONS: &[(&str, &str)] = &[
        ("1.sql", include_str!("../migrations/1.sql")),
        ("2.sql", include_str!("../migrations/2.sql")),
        ("3.sql", include_str!("../migrations/3.sql")),
        ("4.sql", include_str!("../migrations/4.sql")),
        ("5.sql", include_str!("../migrations/5.sql")),
        ("6.sql", include_str!("../migrations/6.sql")),
    ];

    let (client, conn) = tokio_postgres::connect(
        &format!(
            "host=127.0.0.1 port={} user=postgres password=postgres dbname=postgres",
            host_port
        ),
        tokio_postgres::NoTls,
    )
    .await
    .expect("Failed to connect for migrations");

    tokio::spawn(async move {
        if let Err(e) = conn.await {
            eprintln!("migration connection error: {e}");
        }
    });

    for (name, sql) in MIGRATIONS {
        client
            .batch_execute(sql)
            .await
            .unwrap_or_else(|e| panic!("Migration {name} failed: {e}"));
    }
}

async fn execute_preparing<S: my_space_music_catalogue::mapping::Statement>(
    pool: &deadpool_postgres::Pool,
    statement: &S,
) -> Result<S::Result, String> {
    let params = statement.encode_params();
    let client = pool
        .get()
        .await
        .map_err(|e| format!("Pool get: {}", e.to_string()))?;
    let prepared = client
        .prepare_typed_cached(S::SQL, S::PARAM_TYPES)
        .await
        .map_err(|e| {
            format!(
                "Preparation error: {}\nSource: {}",
                e.to_string(),
                e.source()
                    .map_or("unknown".into(), |source| source.to_string())
            )
        })?;
    if S::RETURNS_ROWS {
        let rows = client
            .query(&prepared, params.as_ref())
            .await
            .map_err(|e| format!("Query: {}", e.to_string()))?;
        let affected = rows.len() as u64;
        S::decode_result(rows, affected).map_err(|e| format!("Decoding: {}", e.to_string()))
    } else {
        let affected = client
            .execute(&prepared, params.as_ref())
            .await
            .map_err(|e| format!("Execution: {}", e.to_string()))?;
        S::decode_result(vec![], affected).map_err(|e| format!("Decoding: {}", e.to_string()))
    }
}

#[tokio::test]
async fn insert_album_executes_with_realistic_values() {
    let pool = shared_pool().await;
    execute_preparing(
        &pool,
        &statements::insert_album::Input {
            name: "hello world".to_string(),
            released: chrono::NaiveDate::from_ymd_opt(2024, 1, 15).unwrap(),
            format: Default::default(),
            recording: Default::default(),
        }
    )
    .await
    .unwrap_or_else(|e| panic!("Statement should execute successfully: {e}"));
}


#[tokio::test]
async fn insert_multiple_albums_executes_with_realistic_values() {
    let pool = shared_pool().await;
    execute_preparing(
        &pool,
        &statements::insert_multiple_albums::Input {
            name: vec!["hello world".to_string()],
            released: vec![chrono::NaiveDate::from_ymd_opt(2024, 1, 15).unwrap()],
            format: vec![Default::default()],
        }
    )
    .await
    .unwrap_or_else(|e| panic!("Statement should execute successfully: {e}"));
}


#[tokio::test]
async fn select_album_by_format_executes_with_realistic_values() {
    let pool = shared_pool().await;
    execute_preparing(
        &pool,
        &statements::select_album_by_format::Input {
            format: Default::default(),
        }
    )
    .await
    .unwrap_or_else(|e| panic!("Statement should execute successfully: {e}"));
}


#[tokio::test]
async fn select_album_by_id_executes_with_realistic_values() {
    let pool = shared_pool().await;
    execute_preparing(
        &pool,
        &statements::select_album_by_id::Input {
            id: Some(42i64),
        }
    )
    .await
    .unwrap_or_else(|e| panic!("Statement should execute successfully: {e}"));
}


#[tokio::test]
async fn select_album_by_name_executes_with_realistic_values() {
    let pool = shared_pool().await;
    execute_preparing(
        &pool,
        &statements::select_album_by_name::Input {
            name: "hello world".to_string(),
        }
    )
    .await
    .unwrap_or_else(|e| panic!("Statement should execute successfully: {e}"));
}


#[tokio::test]
async fn select_album_rows_executes_with_realistic_values() {
    let pool = shared_pool().await;
    execute_preparing(
        &pool,
        &statements::select_album_rows::Input::default()
    )
    .await
    .unwrap_or_else(|e| panic!("Statement should execute successfully: {e}"));
}


#[tokio::test]
async fn select_album_with_filters_executes_with_realistic_values() {
    let pool = shared_pool().await;
    execute_preparing(
        &pool,
        &statements::select_album_with_filters::Input {
            include_name: true,
            include_released: true,
            include_format: true,
            include_recording: true,
            include_tracks: true,
            include_disc: true,
            artist_name: Some("hello world".to_string()),
            genre_name: Some("hello world".to_string()),
            format: Some(Default::default()),
            released_after: Some(chrono::NaiveDateTime::new(chrono::NaiveDate::from_ymd_opt(2024, 1, 15).unwrap(), chrono::NaiveTime::from_hms_opt(12, 30, 45).unwrap())),
            name_like: Some("hello world".to_string()),
            order_by_name: true,
            order_by_released: true,
        }
    )
    .await
    .unwrap_or_else(|e| panic!("Statement should execute successfully: {e}"));
}


#[tokio::test]
async fn select_album_with_tracks_executes_with_realistic_values() {
    let pool = shared_pool().await;
    execute_preparing(
        &pool,
        &statements::select_album_with_tracks::Input {
            id: 42i64,
        }
    )
    .await
    .unwrap_or_else(|e| panic!("Statement should execute successfully: {e}"));
}


#[tokio::test]
async fn select_genre_by_artist_executes_with_realistic_values() {
    let pool = shared_pool().await;
    execute_preparing(
        &pool,
        &statements::select_genre_by_artist::Input {
            artist: 42i32,
        }
    )
    .await
    .unwrap_or_else(|e| panic!("Statement should execute successfully: {e}"));
}


#[tokio::test]
async fn update_album_recording_returning_executes_with_realistic_values() {
    let pool = shared_pool().await;
    execute_preparing(
        &pool,
        &statements::update_album_recording_returning::Input {
            recording: Some(Default::default()),
            id: 42i64,
        }
    )
    .await
    .unwrap_or_else(|e| panic!("Statement should execute successfully: {e}"));
}


#[tokio::test]
async fn update_album_released_executes_with_realistic_values() {
    let pool = shared_pool().await;
    execute_preparing(
        &pool,
        &statements::update_album_released::Input {
            released: Some(chrono::NaiveDate::from_ymd_opt(2024, 1, 15).unwrap()),
            id: 42i64,
        }
    )
    .await
    .unwrap_or_else(|e| panic!("Statement should execute successfully: {e}"));
}

