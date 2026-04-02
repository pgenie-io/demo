use tokio_postgres::types::Type;

/// Parameters for the `select_album_with_tracks` query.
///
/// # SQL Template
///
/// ```sql
/// select id, name, tracks, disc
/// from album
/// where id = $id
/// ```
///
/// # Source Path
///
/// `./queries/select_album_with_tracks.sql`
#[derive(Debug, Clone, PartialEq, Default)]
pub struct Input {
    /// Maps to `$id` in the template.
    pub id: i64,
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
    /// Maps to the `tracks` result set column.
    pub tracks: Vec<crate::types::TrackInfo>,
    /// Maps to the `disc` result set column.
    pub disc: Option<crate::types::DiscInfo>,
}


impl crate::mapping::Statement for Input {
    type Result = Output;

    const RETURNS_ROWS: bool = true;

    const SQL: &str = "select id, name, tracks, disc\n\
                       from album\n\
                       where id = $1";

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
        rows.into_iter()
            .enumerate()
            .map(|(row_index, row)| {
                Ok(OutputRow {
                    id: crate::mapping::decode_cell(&row, row_index, 0)?,
                    name: crate::mapping::decode_cell(&row, row_index, 1)?,
                    tracks: crate::mapping::decode_cell(&row, row_index, 2)?,
                    disc: crate::mapping::decode_cell(&row, row_index, 3)?,
                })
            })
            .collect()
    }
}
