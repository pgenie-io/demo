#!/bin/bash
set -eo pipefail

function regenerate {
  rm -rf output
  pgenie
}

function build_haskell_hasql {
  cd output/haskell-hasql
  echo "resolver: nightly-2022-04-14" > stack.yaml
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
