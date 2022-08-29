# Summary

Demonstration of pGenie.

This project defines a music catalogue database using migration files in plain SQL and all the queries that are supposed to be executed by the client applications of this database (they are also defined using plain SQL).

_To see the generated code go to the ["demo-artifacts"](https://github.com/pgenie-io/demo-artifacts) repo._

Following in this readme are instructions on how to use pGenie to generate client code for Java and Haskell.

# Project Structure

- `migrations/` - directory of sequential migrations of the database schema
- `queries/` - directory of queries against the schema that the migrations describe
- `.pgenie1.yaml` - pGenie project configuration

# Running Yourself

- Install [the `pgn` CLI app](https://github.com/pgenie-io/cli) by following the instructions on its repo
- Check out this repo
- Run `pgn`

This will result in creation of the `artifacts` directory. It will contain the generated client packages for Java and Haskell.

Go ahead and play with this project. Try changing the names of columns and their types, introduce some syntactic mistakes and run `pgn` to see the effects.
