use postgres_types::{ToSql, FromSql};

/// Representation of the `recording_info` PostgreSQL composite type.
#[derive(Debug, Clone, PartialEq, Eq, Default, ToSql, FromSql)]
#[postgres(name = "recording_info")]
pub struct RecordingInfo {
    /// Maps to `studio_name`.
    #[postgres(name = "studio_name")]
    pub studio_name: Option<String>,
    /// Maps to `city`.
    #[postgres(name = "city")]
    pub city: Option<String>,
    /// Maps to `country`.
    #[postgres(name = "country")]
    pub country: Option<String>,
    /// Maps to `recorded_date`.
    #[postgres(name = "recorded_date")]
    pub recorded_date: Option<chrono::NaiveDate>,
}
