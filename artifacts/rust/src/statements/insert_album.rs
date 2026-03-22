use tokio_postgres::types::Type;

/// Parameters for the `insert_album` query.
///
/// # SQL Template
///
/// ```sql
/// insert into album (name, released, format, recording)
/// values ($name, $released, $format, $recording)
/// returning id
/// ```
///
/// # Source Path
///
/// `./queries/insert_album.sql`
#[derive(Debug, Clone, PartialEq, Default)]
pub struct Input {
    /// Maps to `$name` in the template.
    pub name: String,
    /// Maps to `$released` in the template.
    pub released: chrono::NaiveDate,
    /// Maps to `$format` in the template.
    pub format: crate::types::AlbumFormat,
    /// Maps to `$recording` in the template.
    pub recording: crate::types::RecordingInfo,
}

/// Result of the statement parameterised by [`Input`].
pub type Output = OutputRow;

/// Row of [`Output`].
#[derive(Debug, Clone, PartialEq)]
pub struct OutputRow {
    /// Maps to the `id` result set column.
    pub id: i64,
}


impl crate::mapping::Statement for Input {
    type Result = Output;

    const RETURNS_ROWS: bool = true;

    const SQL: &str = "insert into album (name, released, format, recording)\n\
                       values ($1, $2, $3::public.album_format, $4::public.recording_info)\n\
                       returning id";

    const PARAM_TYPES: &'static [tokio_postgres::types::Type] = &[Type::TEXT, Type::DATE, Type::UNKNOWN, Type::UNKNOWN];

    #[allow(refining_impl_trait)]
    fn encode_params(
        &self,
    ) -> [&(dyn tokio_postgres::types::ToSql + Sync); Self::PARAM_TYPES.len()] {
        [&self.name, &self.released, &self.format, &self.recording]
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
                    id: crate::mapping::decode_cell(row, 0, 0)?,
                })
            }
            n => Err(crate::mapping::DecodingError::UnexpectedAmountOfRows {
                expected: 1,
                actual: n,
            }),
        }
    }
}
