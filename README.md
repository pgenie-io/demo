# Summary

Demonstration of Pgenie. 

This project defines a music catalogue database using migration files in plain SQL and all the queries that are supposed to be executed by client applications of this database, they are also defined using plain SQL.

Following in this readme are instructions on how to use Pgenie to generate client code for Java and Haskell.

# Project Structure

- `migrations/` - directory of sequential migrations of the database schema
- `queries/` - directory of queries against the schema that the migrations describe
- `.pgn1.yaml` - Pgenie project configuration

# Running

- Install [the `pgn` CLI app](https://github.com/pgenie-io/app) by following the instructions on its repo
- Check out this repo
- Run `pgn`

This will result in creation of the directory `output`. It will contain generated client packages for Java and Haskell. Check them out.
