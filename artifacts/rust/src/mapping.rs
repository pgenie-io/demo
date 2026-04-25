//! Shared PostgreSQL statement mapping primitives.

mod decoding_error;
pub use decoding_error::DecodingError;

/// Implemented by each query's parameter struct. Provides a uniform way to
/// prepare and execute statements against a [`tokio_postgres::Client`].
pub trait Statement {
    /// The type returned when the statement is successfully executed.
    type Result;

    const SQL: &str;

    const PARAM_TYPES: &'static [tokio_postgres::types::Type];

    /// Encode `self` as a list of type-erased parameter references.
    fn encode_params(&self) -> impl AsRef<[&(dyn tokio_postgres::types::ToSql + Sync)]>;

    /// Whether the statement returns rows.
    const RETURNS_ROWS: bool;

    fn decode_result(
        rows: Vec<tokio_postgres::Row>,
        affected_rows: u64,
    ) -> Result<Self::Result, DecodingError>;
}

/// Decode a single result-set cell and attach its row/column location to any
/// PostgreSQL decoding error.
pub fn decode_cell<'a, T: tokio_postgres::types::FromSql<'a>>(
    input_row: &'a tokio_postgres::Row,
    row_index: usize,
    column_index: usize,
) -> Result<T, DecodingError> {
    input_row
        .try_get(column_index)
        .map_err(|source| DecodingError::Cell {
            row: row_index,
            column: column_index,
            source,
        })
}
