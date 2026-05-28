package config

import zio.config._
import zio.config.magnolia._
import zio.config.typesafe._
import zio.{Layer, ZLayer}

final case class MongoConfig(
  uri: String,
  database: String,
  collection: String
)

object MongoConfig {
  val descriptor: ConfigDescriptor[MongoConfig] = descriptor[MongoConfig]

  val layer: Layer[Throwable, MongoConfig] =
    TypesafeConfig.fromResourcePath(descriptor).orDie
}
