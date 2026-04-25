use super::DecodingError;

pub enum Error {
    Decoding(DecodingError),
    Deadpool(deadpool_postgres::PoolError),
    Postgres(tokio_postgres::Error),
}

impl From<DecodingError> for Error {
    fn from(value: DecodingError) -> Self {
        Self::Decoding(value)
    }
}
impl From<deadpool_postgres::PoolError> for Error {
    fn from(value: deadpool_postgres::PoolError) -> Self {
        Self::Deadpool(value)
    }
}
impl From<tokio_postgres::Error> for Error {
    fn from(value: tokio_postgres::Error) -> Self {
        Self::Postgres(value)
    }
}
