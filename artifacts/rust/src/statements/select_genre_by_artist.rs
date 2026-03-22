use tokio_postgres::types::Type;

/// Parameters for the `select_genre_by_artist` query.
///
/// # SQL Template
///
/// ```sql
/// select id, genre.name
/// from genre
/// left join album_genre on album_genre.genre = genre.id
/// left join album_artist on album_artist.album = album_genre.album
/// where album_artist.artist = $artist
/// ```
///
/// # Source Path
///
/// `./queries/select_genre_by_artist.sql`
#[derive(Debug, Clone, PartialEq, Default)]
pub struct Input {
    /// Maps to `$artist` in the template.
    pub artist: i32,
}

/// Result of the statement parameterised by [`Input`].
pub type Output = Vec<OutputRow>;

/// Row of [`Output`].
#[derive(Debug, Clone, PartialEq)]
pub struct OutputRow {
    /// Maps to the `id` result set column.
    pub id: i32,
    /// Maps to the `name` result set column.
    pub name: String,
}


impl crate::mapping::Statement for Input {
    type Result = Output;

    const RETURNS_ROWS: bool = true;

    const SQL: &str = "select id, genre.name\n\
                       from genre\n\
                       left join album_genre on album_genre.genre = genre.id\n\
                       left join album_artist on album_artist.album = album_genre.album\n\
                       where album_artist.artist = $1";

    const PARAM_TYPES: &'static [tokio_postgres::types::Type] = &[Type::INT4];

    #[allow(refining_impl_trait)]
    fn encode_params(
        &self,
    ) -> [&(dyn tokio_postgres::types::ToSql + Sync); Self::PARAM_TYPES.len()] {
        [&self.artist]
    }

    fn decode_result(
        rows: Vec<tokio_postgres::Row>,
        _affected_rows: u64,
    ) -> Result<Self::Result, crate::mapping::DecodingError> {
        rows.into_iter()
            .enumerate()
            .map(|(row_index, row)| {
                Ok(OutputRow {
                    id: crate::mapping::decode_cell(&row, row_index, 0)?,
                    name: crate::mapping::decode_cell(&row, row_index, 1)?,
                })
            })
            .collect()
    }
}
