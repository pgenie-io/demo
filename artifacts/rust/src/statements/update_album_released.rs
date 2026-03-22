use tokio_postgres::types::Type;

/// Parameters for the `update_album_released` query.
///
/// # SQL Template
///
/// ```sql
/// update album
/// set released = $released
/// where id = $id
/// ```
///
/// # Source Path
///
/// `./queries/update_album_released.sql`
#[derive(Debug, Clone, PartialEq, Default)]
pub struct Input {
    /// Maps to `$released` in the template.
    pub released: Option<chrono::NaiveDate>,
    /// Maps to `$id` in the template.
    pub id: i64,
}

/// Result of the statement parameterised by [`Input`].
///
/// Contains the number of rows affected by the statement.
pub type Output = u64;

impl crate::mapping::Statement for Input {
    type Result = Output;

    const RETURNS_ROWS: bool = false;

    const SQL: &str = "update album\n\
                       set released = $1\n\
                       where id = $2";

    const PARAM_TYPES: &'static [tokio_postgres::types::Type] = &[Type::DATE, Type::INT8];

    #[allow(refining_impl_trait)]
    fn encode_params(
        &self,
    ) -> [&(dyn tokio_postgres::types::ToSql + Sync); Self::PARAM_TYPES.len()] {
        [&self.released, &self.id]
    }

    fn decode_result(
        _rows: Vec<tokio_postgres::Row>,
        affected_rows: u64,
    ) -> Result<Self::Result, crate::mapping::DecodingError> {
        Ok(affected_rows)
    }
}
