name := "zio-task-api"

version := "0.1.0-SNAPSHOT"

scalaVersion := "3.5.2"

val zioVersion        = "2.1.15"
val zioHttpVersion    = "3.0.0-RC4"   // Downgraded for API compatibility with current code
val zioJsonVersion    = "0.7.3"
val zioLoggingVersion = "2.3.1"
val zioConfigVersion  = "4.0.2"
val mongoVersion      = "5.1.0"     // Official MongoDB Scala Driver (use _2.13 because it's not published for Scala 3)
val zioTestVersion    = zioVersion

libraryDependencies ++= Seq(
  // Core
  "dev.zio" %% "zio"          % zioVersion,
  "dev.zio" %% "zio-http"     % zioHttpVersion,
  "dev.zio" %% "zio-json"     % zioJsonVersion,

  // Logging
  "dev.zio" %% "zio-logging"       % zioLoggingVersion,
  "dev.zio" %% "zio-logging-slf4j" % zioLoggingVersion,

  // Configuration
  "dev.zio" %% "zio-config"          % zioConfigVersion,
  "dev.zio" %% "zio-config-magnolia" % zioConfigVersion,
  "dev.zio" %% "zio-config-typesafe" % zioConfigVersion,

  // MongoDB - Official MongoDB Scala Driver (forced 2.13 because no Scala 3 artifacts exist)
  "org.mongodb.scala" % "mongo-scala-driver_2.13" % mongoVersion,

  // Test
  "dev.zio" %% "zio-test"     % zioTestVersion % Test,
  "dev.zio" %% "zio-test-sbt" % zioTestVersion % Test
)

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
