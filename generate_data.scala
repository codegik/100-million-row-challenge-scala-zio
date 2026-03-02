#!/usr/bin/env -S scala-cli shebang

import java.io.{BufferedWriter, FileWriter}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.util.Random

@main def generateData(rows: Int = 100_000_000): Unit =
  val urls = Vector(
    "https://example.com/blog/scala-zio",
    "https://example.com/blog/functional-programming",
    "https://example.com/api/users",
    "https://example.com/api/products",
    "https://example.com/docs/getting-started",
    "https://example.com/docs/advanced",
    "https://example.com/",
    "https://example.com/contact",
    "https://example.com/about",
    "https://example.com/pricing"
  )

  val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
  val startDate = LocalDateTime.of(2024, 1, 1, 0, 0)
  val random = new Random(42)

  val writer = new BufferedWriter(new FileWriter("measurements.txt"), 8 * 1024 * 1024)

  println(s"Generating $rows rows...")

  for i <- 1 to rows do
    val url = urls(random.nextInt(urls.length))
    val daysOffset = random.nextInt(365)
    val hoursOffset = random.nextInt(24)
    val minutesOffset = random.nextInt(60)
    val secondsOffset = random.nextInt(60)

    val timestamp = startDate
      .plusDays(daysOffset)
      .plusHours(hoursOffset)
      .plusMinutes(minutesOffset)
      .plusSeconds(secondsOffset)
      .format(formatter)

    writer.write(s"$url,$timestamp\n")

    if i % 10_000_000 == 0 then
      println(s"Generated $i rows...")

  writer.close()
  println(s"Generated $rows rows in measurements.txt")
