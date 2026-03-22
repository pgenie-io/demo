use tokio_postgres::{types::FromSql, Row};

#[derive(Debug)]
pub enum DecodingError {
    UnexpectedAmountOfRows {
        expected: usize,
        actual: usize,
    },
    Cell {
        row: usize,
        column: usize,
        source: tokio_postgres::Error,
    },
}

impl std::fmt::Display for DecodingError {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            DecodingError::UnexpectedAmountOfRows { expected, actual } => {
                write!(f, "expected {expected} row(s), got {actual}")
            }
            DecodingError::Cell {
                row,
                column,
                source: error,
            } => {
                write!(f, "error at row {row}, column {column}: {error}")
            }
        }
    }
}

impl std::error::Error for DecodingError {
    fn source(&self) -> Option<&(dyn std::error::Error + 'static)> {
        match self {
            DecodingError::Cell { source: error, .. } => Some(error),
            DecodingError::UnexpectedAmountOfRows { .. } => None,
        }
    }
}

/// Decode a single result-set cell and attach its row/column location to any
/// PostgreSQL decoding error.
pub fn decode_cell<'a, T: FromSql<'a>>(
    input_row: &'a Row,
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
