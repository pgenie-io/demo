use tokio_postgres::types::Type;

/// Parameters for the `select_album_by_name` query.
///
/// # SQL Template
///
/// ```sql
/// select 
///   id,
///   name,
///   released,
///   format,
///   recording
/// from album
/// where name = $name
/// ```
///
/// # Source Path
///
/// `./queries/select_album_by_name.sql`
#[derive(Debug, Clone, PartialEq, Default)]
pub struct Input {
    /// Maps to `$name` in the template.
    pub name: String,
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
    /// Maps to the `recording` result set column.
    pub recording: Option<crate::types::RecordingInfo>,
}


impl crate::mapping::Statement for Input {
    type Result = Output;

    const RETURNS_ROWS: bool = true;

    const SQL: &str = "select \n\
                         id,\n\
                         name,\n\
                         released,\n\
                         format,\n\
                         recording\n\
                       from album\n\
                       where name = $1";

    const PARAM_TYPES: &'static [tokio_postgres::types::Type] = &[Type::TEXT];

    #[allow(refining_impl_trait)]
    fn encode_params(
        &self,
    ) -> [&(dyn tokio_postgres::types::ToSql + Sync); Self::PARAM_TYPES.len()] {
        [&self.name]
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
                    recording: crate::mapping::decode_cell(&row, row_index, 4)?,
                })
            })
            .collect()
    }
}
