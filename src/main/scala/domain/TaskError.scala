package domain

import zio.json._

sealed trait TaskError extends Throwable {
  def message: String
  override def getMessage: String = message
}

object TaskError {
  final case class TaskNotFound(id: TaskId) extends TaskError {
    val message = s"Task with id ${id.value} not found"
  }

  final case class ValidationError(field: String, reason: String) extends TaskError {
    val message = s"Validation error on '$field': $reason"
  }

  implicit val encoder: JsonEncoder[TaskError] = DeriveJsonEncoder.gen[TaskError]
  implicit val decoder: JsonDecoder[TaskError] = DeriveJsonDecoder.gen[TaskError]
}
