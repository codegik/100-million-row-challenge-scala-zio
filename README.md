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

### Generate test data

```bash
./generate.sh 100000000
```

### Run the challenge

```bash
./run.sh measurements.txt
```

## Performance

Results will vary based on hardware. The implementation is optimized for:
- Multi-core CPU utilization
- Efficient memory usage with streaming
- Minimal allocations during processing
