# Summary

Project demonstrating the use of Pgenie. It defines a music catalogue database with a bunch of queries against it. Following are instructions on how to run Pgenie on it.

# Structure

- `migrations/` - directory of sequential migrations of the database schema
- `queries/` - directory of queries against the schema that the migrations describe
- `.pgn1.yaml` - Pgenie project configuration

# Running

- Install [the `pgn` CLI app](https://github.com/pgenie-io/app) by following the instructions on its repo
- Check out this repo
- Run `pgn`

This will result in creation of the directory `output`. It will contain generated client packages for Java and Haskell. Check them out.
