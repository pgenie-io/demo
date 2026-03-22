use postgres_types::{ToSql, FromSql};

/// Representation of the `track_info` PostgreSQL composite type.
#[derive(Debug, Clone, PartialEq, Eq, Default, ToSql, FromSql)]
#[postgres(name = "track_info")]
pub struct TrackInfo {
    /// Maps to `title`.
    #[postgres(name = "title")]
    pub title: Option<String>,
    /// Maps to `duration_seconds`.
    #[postgres(name = "duration_seconds")]
    pub duration_seconds: Option<i32>,
    /// Maps to `tags`.
    #[postgres(name = "tags")]
    pub tags: Option<Vec<Option<String>>>,
}
