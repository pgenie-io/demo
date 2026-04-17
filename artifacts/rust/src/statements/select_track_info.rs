use tokio_postgres::types::Type;

/// Parameters for the `select_track_info` query.
///
/// # SQL Template
///
/// ```sql
/// select
///   (
///     'title',
///     180,
///     array['a', 'b']
///   )::track_info as track_info,
///   234 as id
/// ```
///
/// # Source Path
///
/// `./queries/select_track_info.sql`
#[derive(Debug, Clone, PartialEq, Default)]
pub struct Input;

/// Result of the statement parameterised by [`Input`].
pub type Output = OutputRow;

/// Row of [`Output`].
#[derive(Debug, Clone, PartialEq)]
pub struct OutputRow {
    /// Maps to the `track_info` result set column.
    pub track_info: Option<crate::types::TrackInfo>,
    /// Maps to the `id` result set column.
    pub id: Option<i32>,
}


impl crate::mapping::Statement for Input {
    type Result = Output;

    const RETURNS_ROWS: bool = true;

    const SQL: &str = "select\n\
                         (\n\
                           'title',\n\
                           180,\n\
                           array['a', 'b']\n\
                         )::track_info as track_info,\n\
                         234 as id";

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
        match rows.len() {
            0 => Err(crate::mapping::DecodingError::UnexpectedAmountOfRows {
                expected: 1,
                actual: 0,
            }),
            1 => {
                let row = rows.first().unwrap();
                Ok(OutputRow {
                    track_info: crate::mapping::decode_cell(row, 0, 0)?,
                    id: crate::mapping::decode_cell(row, 0, 1)?,
                })
            }
            n => Err(crate::mapping::DecodingError::UnexpectedAmountOfRows {
                expected: 1,
                actual: n,
            }),
        }
    }
}
