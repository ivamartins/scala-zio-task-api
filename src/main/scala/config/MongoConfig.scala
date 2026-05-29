package config

import zio.{Layer, ZLayer}

final case class MongoConfig(
  uri: String,
  database: String,
  collection: String
)

object MongoConfig {

  // For Docker Compose setup (admin/admin123)
  val default: MongoConfig = MongoConfig(
    uri = "mongodb://admin:admin123@localhost:27017",
    database = "taskdb",
    collection = "tasks"
  )

  val layer: Layer[Nothing, MongoConfig] =
    ZLayer.succeed(default)
}
