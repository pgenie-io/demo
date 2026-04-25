//! Shared PostgreSQL statement mapping primitives.

mod decoding_error;
mod error;

use std::future::Future;

pub use decoding_error::DecodingError;
pub use error::Error;

/// Implemented by each query's parameter struct. Provides a uniform way to
/// prepare and execute statements against a [`tokio_postgres::Client`].
pub trait Statement {
    /// The type returned when the statement is successfully executed.
    type Result;

    const SQL: &str;

    const PARAM_TYPES: &'static [tokio_postgres::types::Type];

    /// Encode `self` as a list of type-erased parameter references.
    fn encode_params(&self) -> impl AsRef<[&(dyn tokio_postgres::types::ToSql + Sync)]> + Send;

    /// Whether the statement returns rows.
    const RETURNS_ROWS: bool;

    fn decode_result(
        rows: Vec<tokio_postgres::Row>,
        affected_rows: u64,
    ) -> Result<Self::Result, DecodingError>;

    /// Execute the statement without preparing it first. This is less efficient than `execute_preparing` but is supported by all PostgreSQL proxies.
    fn execute_without_preparing(
        &self,
        client: &deadpool_postgres::Client,
    ) -> impl Future<Output = Result<Self::Result, Error>> + Send {
        let params = self.encode_params();
        async move {
            let params_borrowed = params.as_ref();

            if Self::RETURNS_ROWS {
                let rows = client.query(Self::SQL, params_borrowed).await?;
                let affected = rows.len() as u64;
                Self::decode_result(rows, affected).map_err(Error::Decoding)
            } else {
                let affected = client.execute(Self::SQL, params_borrowed).await?;
                Self::decode_result(Vec::new(), affected).map_err(Error::Decoding)
            }
        }
    }

    /// Execute the statement automatically preparing it if necessary.
    /// This is a more efficient way to execute parameteric statements, however it is unsupported by some PostgreSQL proxies like the older versions of `pgbouncer`.
    /// 
    /// Internally utilizes a prepared statement cache implemented by `deadpool-postgres`.
    fn execute_preparing(
        &self,
        client: &deadpool_postgres::Client,
    ) -> impl Future<Output = Result<Self::Result, Error>> + Send {
        let params = self.encode_params();
        async move {
            let params_borrowed = params.as_ref();

            let prepared = client
                .prepare_typed_cached(Self::SQL, Self::PARAM_TYPES)
                .await?;

            if Self::RETURNS_ROWS {
                let rows = client.query(&prepared, params_borrowed).await?;
                let affected = rows.len() as u64;
                Self::decode_result(rows, affected).map_err(Error::Decoding)
            } else {
                let affected = client.execute(&prepared, params_borrowed).await?;
                Self::decode_result(Vec::new(), affected).map_err(Error::Decoding)
            }
        }
    }
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
