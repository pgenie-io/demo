use tokio_postgres::types::Type;

/// Parameters for the `select_album_fields` query.
///
/// # SQL Template
///
/// ```sql
/// -- Demonstrates static query equivalent of dynamic field selection.
/// -- Boolean flags control which fields are included in the result,
/// -- returning NULL for fields the caller opts out of.
/// SELECT
///   album.id,
///   CASE WHEN $include_name      THEN album.name      END AS name,
///   CASE WHEN $include_released  THEN album.released  END AS released,
///   CASE WHEN $include_format    THEN album.format    END AS format,
///   CASE WHEN $include_recording THEN album.recording END AS recording,
///   CASE WHEN $include_tracks    THEN album.tracks    END AS tracks,
///   CASE WHEN $include_disc      THEN album.disc      END AS disc
/// FROM album
/// WHERE album.id = $id
/// ```
///
/// # Source Path
///
/// `./queries/select_album_fields.sql`
#[derive(Debug, Clone, PartialEq, Default)]
pub struct Input {
    /// Maps to `$include_name` in the template.
    pub include_name: bool,
    /// Maps to `$include_released` in the template.
    pub include_released: bool,
    /// Maps to `$include_format` in the template.
    pub include_format: bool,
    /// Maps to `$include_recording` in the template.
    pub include_recording: bool,
    /// Maps to `$include_tracks` in the template.
    pub include_tracks: bool,
    /// Maps to `$include_disc` in the template.
    pub include_disc: bool,
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
    pub name: Option<String>,
    /// Maps to the `released` result set column.
    pub released: Option<chrono::NaiveDate>,
    /// Maps to the `format` result set column.
    pub format: Option<crate::types::AlbumFormat>,
    /// Maps to the `recording` result set column.
    pub recording: Option<crate::types::RecordingInfo>,
    /// Maps to the `tracks` result set column.
    pub tracks: Option<Vec<crate::types::TrackInfo>>,
    /// Maps to the `disc` result set column.
    pub disc: Option<crate::types::DiscInfo>,
}


impl crate::mapping::Statement for Input {
    type Result = Output;

    const RETURNS_ROWS: bool = true;

    const SQL: &str = "-- Demonstrates static query equivalent of dynamic field selection.\n\
                       -- Boolean flags control which fields are included in the result,\n\
                       -- returning NULL for fields the caller opts out of.\n\
                       SELECT\n\
                         album.id,\n\
                         CASE WHEN $1      THEN album.name      END AS name,\n\
                         CASE WHEN $2  THEN album.released  END AS released,\n\
                         CASE WHEN $3    THEN album.format    END AS format,\n\
                         CASE WHEN $4 THEN album.recording END AS recording,\n\
                         CASE WHEN $5    THEN album.tracks    END AS tracks,\n\
                         CASE WHEN $6      THEN album.disc      END AS disc\n\
                       FROM album\n\
                       WHERE album.id = $7";

    const PARAM_TYPES: &'static [tokio_postgres::types::Type] = &[Type::BOOL, Type::BOOL, Type::BOOL, Type::BOOL, Type::BOOL, Type::BOOL, Type::INT8];

    #[allow(refining_impl_trait)]
    fn encode_params(
        &self,
    ) -> [&(dyn tokio_postgres::types::ToSql + Sync); Self::PARAM_TYPES.len()] {
        [&self.include_name, &self.include_released, &self.include_format, &self.include_recording, &self.include_tracks, &self.include_disc, &self.id]
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
                    tracks: crate::mapping::decode_cell(&row, row_index, 5)?,
                    disc: crate::mapping::decode_cell(&row, row_index, 6)?,
                })
            })
            .collect()
    }
}
