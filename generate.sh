#!/bin/bash

set -e

ROWS="${1:-100000000}"

echo "Generating $ROWS rows of test data..."

if ! command -v scala-cli &> /dev/null; then
    echo "Error: scala-cli not found. Installing via brew..."
    brew install Virtuslab/scala-cli/scala-cli
fi

scala-cli run generate_data.scala -- $ROWS
