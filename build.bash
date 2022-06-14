#!/bin/bash
set -eo pipefail

function regenerate {
  rm -rf output/java-jdbc/src
  rm -rf output/haskell-hasql/library
  pgn-dev
}

function build_haskell_hasql {
  cd output/haskell-hasql
  echo "resolver: nightly-2022-06-08" > stack.yaml
  stack build
  cd -
}

function build_java_jdbc {
  cd output/java-jdbc
  mvn compile
  cd -
}

regenerate
build_java_jdbc
build_haskell_hasql
