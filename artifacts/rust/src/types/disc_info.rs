use postgres_types::{ToSql, FromSql};

/// Representation of the `disc_info` PostgreSQL composite type.
#[derive(Debug, Clone, PartialEq, Eq, Default, ToSql, FromSql)]
#[postgres(name = "disc_info")]
pub struct DiscInfo {
    /// Maps to `name`.
    #[postgres(name = "name")]
    pub name: Option<String>,
    /// Maps to `recording`.
    #[postgres(name = "recording")]
    pub recording: Option<crate::types::RecordingInfo>,
}
