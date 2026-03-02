#!/bin/bash

set -e

INPUT_FILE="${1:-measurements.txt}"

if [ ! -f "$INPUT_FILE" ]; then
    echo "Error: Input file $INPUT_FILE not found"
    echo "Usage: ./run.sh [input_file]"
    exit 1
fi

echo "Building project..."
sbt compile

echo "Running challenge..."
sbt "run $INPUT_FILE"
