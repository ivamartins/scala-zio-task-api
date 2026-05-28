package api

import zio.http._
import zio.json._

object HealthRoutes {

  final case class HealthStatus(status: String, service: String)

  object HealthStatus {
    implicit val encoder: JsonEncoder[HealthStatus] = DeriveJsonEncoder.gen[HealthStatus]
  }

  val routes: Routes[Any, Response] = Routes(
    Method.GET / "health" ->
      handler {
        Response.json(HealthStatus("ok", "zio-task-api").toJson)
      }
  )
}
