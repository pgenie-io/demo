//! Type-safe bindings for the `music_catalogue` database.
//!
//! Generated from SQL queries using the [pGenie](https://pgenie.io) code generator.
//!
//! - [`statements`] – ready-to-use statement definitions for all queries with
//!   associated parameter and result types.
//! - [`mapping`] – shared PostgreSQL statement mapping primitives used by the
//!   generated statements and tests.
//! - [`types`] – PostgreSQL enum and composite type mappings.

pub mod mapping;
pub mod statements;
pub mod types;
