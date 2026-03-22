use postgres_types::{FromSql, ToSql};

/// Representation of the `album_format` user-declared PostgreSQL enumeration type.
#[derive(Debug, Clone, Copy, PartialEq, Eq, PartialOrd, Ord, Hash, ToSql, FromSql)]
#[postgres(name = "album_format")]
#[derive(Default)]
pub enum AlbumFormat {
    /// Corresponds to the PostgreSQL enum variant `Vinyl`.
    #[postgres(name = "Vinyl")]
    #[default]
    Vinyl,
    /// Corresponds to the PostgreSQL enum variant `CD`.
    #[postgres(name = "CD")]
    Cd,
    /// Corresponds to the PostgreSQL enum variant `Cassette`.
    #[postgres(name = "Cassette")]
    Cassette,
    /// Corresponds to the PostgreSQL enum variant `Digital`.
    #[postgres(name = "Digital")]
    Digital,
    /// Corresponds to the PostgreSQL enum variant `DVD-Audio`.
    #[postgres(name = "DVD-Audio")]
    DvdAudio,
    /// Corresponds to the PostgreSQL enum variant `SACD`.
    #[postgres(name = "SACD")]
    Sacd,
}
