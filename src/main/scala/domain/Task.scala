package domain

import zio.json._

import java.time.Instant

case class TaskId(value: Long) extends AnyVal

object TaskId {
  implicit val encoder: JsonEncoder[TaskId] = JsonEncoder.long.contramap(_.value)
  implicit val decoder: JsonDecoder[TaskId] = JsonDecoder.long.map(TaskId(_))
}

case class Task(
  id: TaskId,
  title: String,
  description: Option[String],
  completed: Boolean,
  createdAt: Instant,
  updatedAt: Instant
)

object Task {
  implicit val encoder: JsonEncoder[Task] = DeriveJsonEncoder.gen[Task]
  implicit val decoder: JsonDecoder[Task] = DeriveJsonDecoder.gen[Task]

  def create(title: String, description: Option[String]): Task = {
    val now = Instant.now()
    Task(
      id = TaskId(0),
      title = title,
      description = description,
      completed = false,
      createdAt = now,
      updatedAt = now
    )
  }
}
