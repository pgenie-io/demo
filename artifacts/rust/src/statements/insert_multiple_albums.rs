use tokio_postgres::types::Type;

/// Parameters for the `insert_multiple_albums` query.
///
/// # SQL Template
///
/// ```sql
/// -- This is an example of a bulk-insert (batch-insert) technique.
/// -- We pass in all fields as arrays of the same size, and we unnest it to insert multiple rows at once.
/// insert into album (name, released, format)
/// select *
/// from unnest(
///   $name::text[],
///   $released::date[],
///   $format::album_format[]
/// )
/// returning id
/// ```
///
/// # Source Path
///
/// `./queries/insert_multiple_albums.sql`
#[derive(Debug, Clone, PartialEq, Default)]
pub struct Input {
    /// Maps to `$name` in the template.
    pub name: Vec<String>,
    /// Maps to `$released` in the template.
    pub released: Vec<chrono::NaiveDate>,
    /// Maps to `$format` in the template.
    pub format: Vec<crate::types::AlbumFormat>,
}

/// Result of the statement parameterised by [`Input`].
pub type Output = Vec<OutputRow>;

/// Row of [`Output`].
#[derive(Debug, Clone, PartialEq)]
pub struct OutputRow {
    /// Maps to the `id` result set column.
    pub id: i64,
}


impl crate::mapping::Statement for Input {
    type Result = Output;

    const RETURNS_ROWS: bool = true;

    const SQL: &str = "-- This is an example of a bulk-insert (batch-insert) technique.\n\
                       -- We pass in all fields as arrays of the same size, and we unnest it to insert multiple rows at once.\n\
                       insert into album (name, released, format)\n\
                       select *\n\
                       from unnest(\n\
                         $1::text[],\n\
                         $2::date[],\n\
                         $3::public.album_format[]::album_format[]\n\
                       )\n\
                       returning id";

    const PARAM_TYPES: &'static [tokio_postgres::types::Type] = &[Type::TEXT_ARRAY, Type::DATE_ARRAY, Type::UNKNOWN];

    #[allow(refining_impl_trait)]
    fn encode_params(
        &self,
    ) -> [&(dyn tokio_postgres::types::ToSql + Sync); Self::PARAM_TYPES.len()] {
        [&self.name, &self.released, &self.format]
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
                })
            })
            .collect()
    }
}
