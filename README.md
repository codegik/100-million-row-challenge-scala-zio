# 100-million-row-challenge-scala-zio

Scala ZIO implementation of the 100 million row challenge for processing web analytics data.

## Challenge

Process 100 million rows of web analytics data (URL + timestamp) and aggregate visit counts by URL path and date.

**Input Format:**
```
https://example.com/blog/scala-zio,2024-01-24T01:16:58+00:00
```

**Output Format:**
```json
{
  "/blog/scala-zio": {
    "2024-01-24": 1
  }
}
```

## Implementation

Uses ZIO Streams with parallel processing:
- Parallel processing with configurable parallelism
- ZIO Streams for efficient file reading
- Chunked processing with parallel fibers
- Concurrent aggregation and merging
- Sorted JSON output

## Requirements

- Java 11 or higher
- sbt 1.9.7
- scala-cli (for data generation)

## Usage

### Quick test

```bash
make test
```

This runs the implementation with a small test file to verify correctness.

### Generate test data

```bash
make generate
```

This generates 100 million rows of test data in `measurements.txt`.

Alternatively, specify a custom number of rows:

```bash
./generate.sh 10000000
```

### Run the challenge

```bash
make run
```

Or directly:

```bash
./run.sh measurements.txt
```

### Clean

```bash
make clean
```

## Performance

Results will vary based on hardware. The implementation is optimized for:
- Multi-core CPU utilization via parallel fiber processing
- Efficient memory usage with ZIO Streams
- Chunked processing to balance memory and throughput
- Minimal allocations during aggregation

Expected performance on modern hardware:
- 100M rows: ~2-5 seconds (depending on CPU cores and disk I/O)

Compare with the Zig implementation (0.765 seconds) to evaluate JVM overhead and optimization opportunities.
