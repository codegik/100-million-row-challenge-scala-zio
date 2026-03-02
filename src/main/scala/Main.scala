import zio.*
import zio.stream.*
import zio.json.*
import java.nio.file.{Files, Paths}
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption
import java.nio.MappedByteBuffer
import scala.collection.mutable
import java.net.URI

case class VisitCount(path: String, date: String, count: Int)

case class PathStats(visits: Map[String, Int]) derives JsonEncoder

object Main extends ZIOAppDefault:

  def parseUrlPath(url: String): Option[String] =
    try
      val uri = new URI(url)
      val path = uri.getPath
      if path.isEmpty then Some("/") else Some(path)
    catch
      case _: Exception => None

  def parseDate(timestamp: String): Option[String] =
    if timestamp.length >= 10 then
      Some(timestamp.take(10))
    else
      None

  def parseLine(line: String): Option[(String, String)] =
    val parts = line.split(',')
    if parts.length == 2 then
      for
        path <- parseUrlPath(parts(0).trim)
        date <- parseDate(parts(1).trim)
      yield (path, date)
    else
      None

  def processChunk(lines: Chunk[String]): Map[(String, String), Int] =
    val counts = mutable.HashMap.empty[(String, String), Int]
    lines.foreach { line =>
      parseLine(line).foreach { case (path, date) =>
        counts.updateWith((path, date)) {
          case Some(count) => Some(count + 1)
          case None => Some(1)
        }
      }
    }
    counts.toMap

  def mergeMaps(maps: Chunk[Map[(String, String), Int]]): Map[(String, String), Int] =
    val result = mutable.HashMap.empty[(String, String), Int]
    maps.foreach { map =>
      map.foreach { case (key, count) =>
        result.updateWith(key) {
          case Some(existing) => Some(existing + count)
          case None => Some(count)
        }
      }
    }
    result.toMap

  def formatOutput(aggregated: Map[(String, String), Int]): String =
    val grouped = aggregated.groupBy(_._1._1).view.mapValues { entries =>
      entries.map { case ((_, date), count) => (date, count) }.toMap
    }.toMap

    val sorted = grouped.toSeq.sortBy(_._1).map { case (path, dates) =>
      val sortedDates = dates.toSeq.sortBy(_._1)
      (path, PathStats(sortedDates.toMap))
    }.toMap

    sorted.toJson

  def processFile(inputPath: String, parallelism: Int): ZIO[Any, Throwable, String] =
    for
      chunkSize <- ZIO.succeed(10000)
      results <- ZStream
        .fromPath(Paths.get(inputPath))
        .via(ZPipeline.utf8Decode)
        .via(ZPipeline.splitLines)
        .filter(_.nonEmpty)
        .grouped(chunkSize)
        .mapZIOPar(parallelism)(chunk => ZIO.succeed(processChunk(chunk)))
        .runCollect
      merged = mergeMaps(results)
      output = formatOutput(merged)
    yield output

  def run =
    val args = getArgs
    for
      arguments <- args
      inputFile = arguments.headOption.getOrElse("measurements.txt")
      parallelism = java.lang.Runtime.getRuntime.availableProcessors
      _ <- Console.printLine(s"Processing file: $inputFile with parallelism: $parallelism")
      start <- Clock.nanoTime
      output <- processFile(inputFile, parallelism)
      end <- Clock.nanoTime
      duration = (end - start) / 1_000_000_000.0
      outputFile = "output.json"
      _ <- ZIO.attemptBlocking(Files.writeString(Paths.get(outputFile), output))
      _ <- Console.printLine(f"Processed in $duration%.3f seconds")
      _ <- Console.printLine(s"Output written to: $outputFile")
    yield ()
