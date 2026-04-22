use postgres_types::{ToSql, FromSql};

/// Representation of the `album` PostgreSQL composite type.
#[derive(Debug, Clone, PartialEq, Eq, Default, ToSql, FromSql)]
#[postgres(name = "album")]
pub struct Album {
    /// Maps to `id`.
    #[postgres(name = "id")]
    pub id: Option<i64>,
    /// Maps to `name`.
    #[postgres(name = "name")]
    pub name: Option<String>,
    /// Maps to `released`.
    #[postgres(name = "released")]
    pub released: Option<chrono::NaiveDate>,
    /// Maps to `format`.
    #[postgres(name = "format")]
    pub format: Option<crate::types::AlbumFormat>,
    /// Maps to `recording`.
    #[postgres(name = "recording")]
    pub recording: Option<crate::types::RecordingInfo>,
    /// Maps to `tracks`.
    #[postgres(name = "tracks")]
    pub tracks: Option<Vec<Option<crate::types::TrackInfo>>>,
    /// Maps to `disc`.
    #[postgres(name = "disc")]
    pub disc: Option<crate::types::DiscInfo>,
}
