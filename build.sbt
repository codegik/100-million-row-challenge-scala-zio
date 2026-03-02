name := "100-million-row-challenge-scala-zio"

version := "0.1.0"

scalaVersion := "3.3.1"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.0.19",
  "dev.zio" %% "zio-streams" % "2.0.19",
  "dev.zio" %% "zio-json" % "0.6.2"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked"
)

fork := true
javaOptions ++= Seq(
  "-Xmx4g",
  "-Xms4g",
  "-XX:+UseG1GC",
  "-XX:+ParallelRefProcEnabled"
)
