use tokio_postgres::types::Type;

/// Parameters for the `select_album_rows` query.
///
/// # SQL Template
///
/// ```sql
/// select (album.*)::album from album
/// ```
///
/// # Source Path
///
/// `./queries/select_album_rows.sql`
#[derive(Debug, Clone, PartialEq, Default)]
pub struct Input;

/// Result of the statement parameterised by [`Input`].
pub type Output = Vec<OutputRow>;

/// Row of [`Output`].
#[derive(Debug, Clone, PartialEq)]
pub struct OutputRow {
    /// Maps to the `album` result set column.
    pub album: Option<crate::types::Album>,
}


impl crate::mapping::Statement for Input {
    type Result = Output;

    const RETURNS_ROWS: bool = true;

    const SQL: &str = "select (album.*)::album from album";

    const PARAM_TYPES: &'static [tokio_postgres::types::Type] = &[];

    #[allow(refining_impl_trait)]
    fn encode_params(
        &self,
    ) -> [&(dyn tokio_postgres::types::ToSql + Sync); Self::PARAM_TYPES.len()] {
        []
    }

    fn decode_result(
        rows: Vec<tokio_postgres::Row>,
        _affected_rows: u64,
    ) -> Result<Self::Result, crate::mapping::DecodingError> {
        rows.into_iter()
            .enumerate()
            .map(|(row_index, row)| {
                Ok(OutputRow {
                    album: crate::mapping::decode_cell(&row, row_index, 0)?,
                })
            })
            .collect()
    }
}
