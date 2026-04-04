use tokio_postgres::types::Type;

/// Parameters for the `select_album_by_id` query.
///
/// # SQL Template
///
/// ```sql
/// -- Example of a query selecting 0 or 1 row.
/// select *
/// from album
/// where id = $id
/// limit 1
/// ```
///
/// # Source Path
///
/// `./queries/select_album_by_id.sql`
#[derive(Debug, Clone, PartialEq, Default)]
pub struct Input {
    /// Maps to `$id` in the template.
    pub id: Option<i64>,
}

/// Result of the statement parameterised by [`Input`].
pub type Output = Option<OutputRow>;

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
    /// Maps to the `recording` result set column.
    pub recording: Option<crate::types::RecordingInfo>,
    /// Maps to the `tracks` result set column.
    pub tracks: Option<Vec<Option<crate::types::TrackInfo>>>,
    /// Maps to the `disc` result set column.
    pub disc: Option<crate::types::DiscInfo>,
}


impl crate::mapping::Statement for Input {
    type Result = Output;

    const RETURNS_ROWS: bool = true;

    const SQL: &str = "-- Example of a query selecting 0 or 1 row.\n\
                       select *\n\
                       from album\n\
                       where id = $1\n\
                       limit 1";

    const PARAM_TYPES: &'static [tokio_postgres::types::Type] = &[Type::INT8];

    #[allow(refining_impl_trait)]
    fn encode_params(
        &self,
    ) -> [&(dyn tokio_postgres::types::ToSql + Sync); Self::PARAM_TYPES.len()] {
        [&self.id]
    }

    fn decode_result(
        rows: Vec<tokio_postgres::Row>,
        _affected_rows: u64,
    ) -> Result<Self::Result, crate::mapping::DecodingError> {
        match rows.len() {
            0 => Ok(None),
            1 => {
                let row = rows.first().unwrap();
                Ok(Some(OutputRow {
                    id: crate::mapping::decode_cell(row, 0, 0)?,
                    name: crate::mapping::decode_cell(row, 0, 1)?,
                    released: crate::mapping::decode_cell(row, 0, 2)?,
                    format: crate::mapping::decode_cell(row, 0, 3)?,
                    recording: crate::mapping::decode_cell(row, 0, 4)?,
                    tracks: crate::mapping::decode_cell(row, 0, 5)?,
                    disc: crate::mapping::decode_cell(row, 0, 6)?,
                }))
            }
            n => Err(crate::mapping::DecodingError::UnexpectedAmountOfRows {
                expected: 1,
                actual: n,
            }),
        }
    }
}
