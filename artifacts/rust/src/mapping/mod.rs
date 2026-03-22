//! Shared PostgreSQL statement mapping primitives.

mod decoding_error;
pub use decoding_error::{decode_cell, DecodingError};

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
