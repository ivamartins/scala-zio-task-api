name := "zio-task-api"

version := "0.1.0-SNAPSHOT"

scalaVersion := "3.5.2"

val zioVersion        = "2.1.15"
val zioHttpVersion    = "3.0.1"
val zioJsonVersion    = "0.7.3"
val zioLoggingVersion = "2.3.1"
val zioConfigVersion  = "4.0.2"
val mongo4catsVersion = "0.7.12"
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

  // MongoDB (mongo4cats)
  "com.github.mongocamp" %% "mongo4cats-zio"      % mongo4catsVersion,
  "com.github.mongocamp" %% "mongo4cats-circe"    % mongo4catsVersion,

  // Test
  "dev.zio" %% "zio-test"     % zioTestVersion % Test,
  "dev.zio" %% "zio-test-sbt" % zioTestVersion % Test
)

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
