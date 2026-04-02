use tokio_postgres::types::Type;

/// Parameters for the `select_album_with_filters` query.
///
/// # SQL Template
///
/// ```sql
/// SELECT
///   album.id,
///   album.name,
///   album.released,
///   album.format
/// FROM album
/// LEFT JOIN album_artist ON album_artist.album = album.id
/// LEFT JOIN artist ON artist.id = album_artist.artist
/// LEFT JOIN album_genre ON album_genre.album = album.id
/// LEFT JOIN genre ON genre.id = album_genre.genre
/// WHERE
///   ($artist_name::text IS NULL OR artist.name = $artist_name)
///   AND ($genre_name::text IS NULL OR genre.name = $genre_name)
///   AND ($format::album_format IS NULL OR album.format = $format)
///   AND ($released_after::timestamp IS NULL OR album.released >= $released_after)
///   AND ($name_like::text IS NULL OR album.name LIKE $name_like)
/// ORDER BY
///   CASE WHEN $order_by_name THEN album.name END ASC,
///   CASE WHEN $order_by_released THEN album.released END DESC
/// ```
///
/// # Source Path
///
/// `./queries/select_album_with_filters.sql`
#[derive(Debug, Clone, PartialEq, Default)]
pub struct Input {
    /// Maps to `$artist_name` in the template.
    pub artist_name: Option<String>,
    /// Maps to `$genre_name` in the template.
    pub genre_name: Option<String>,
    /// Maps to `$format` in the template.
    pub format: Option<crate::types::AlbumFormat>,
    /// Maps to `$released_after` in the template.
    pub released_after: Option<chrono::NaiveDateTime>,
    /// Maps to `$name_like` in the template.
    pub name_like: Option<String>,
    /// Maps to `$order_by_name` in the template.
    pub order_by_name: bool,
    /// Maps to `$order_by_released` in the template.
    pub order_by_released: bool,
}

/// Result of the statement parameterised by [`Input`].
pub type Output = Vec<OutputRow>;

/// Row of [`Output`].
#[derive(Debug, Clone, PartialEq)]
pub struct OutputRow {
    /// Maps to the `id` result set column.
    pub id: i64,
    /// Maps to the `name` result set column.
    pub name: String,
    /// Maps to the `released` result set column.
    pub released: Option<chrono::NaiveDate>,
    /// Maps to the `format` result set column.
    pub format: Option<crate::types::AlbumFormat>,
}


impl crate::mapping::Statement for Input {
    type Result = Output;

    const RETURNS_ROWS: bool = true;

    const SQL: &str = "SELECT\n\
                         album.id,\n\
                         album.name,\n\
                         album.released,\n\
                         album.format\n\
                       FROM album\n\
                       LEFT JOIN album_artist ON album_artist.album = album.id\n\
                       LEFT JOIN artist ON artist.id = album_artist.artist\n\
                       LEFT JOIN album_genre ON album_genre.album = album.id\n\
                       LEFT JOIN genre ON genre.id = album_genre.genre\n\
                       WHERE\n\
                         ($1::text IS NULL OR artist.name = $1)\n\
                         AND ($2::text IS NULL OR genre.name = $2)\n\
                         AND ($3::public.album_format::album_format IS NULL OR album.format = $3::public.album_format)\n\
                         AND ($4::timestamp IS NULL OR album.released >= $4)\n\
                         AND ($5::text IS NULL OR album.name LIKE $5)\n\
                       ORDER BY\n\
                         CASE WHEN $6 THEN album.name END ASC,\n\
                         CASE WHEN $7 THEN album.released END DESC";

    const PARAM_TYPES: &'static [tokio_postgres::types::Type] = &[Type::TEXT, Type::TEXT, Type::UNKNOWN, Type::TIMESTAMP, Type::TEXT, Type::BOOL, Type::BOOL];

    #[allow(refining_impl_trait)]
    fn encode_params(
        &self,
    ) -> [&(dyn tokio_postgres::types::ToSql + Sync); Self::PARAM_TYPES.len()] {
        [&self.artist_name, &self.genre_name, &self.format, &self.released_after, &self.name_like, &self.order_by_name, &self.order_by_released]
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
                    released: crate::mapping::decode_cell(&row, row_index, 2)?,
                    format: crate::mapping::decode_cell(&row, row_index, 3)?,
                })
            })
            .collect()
    }
}
