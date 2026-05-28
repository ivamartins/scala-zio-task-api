package service

import domain.{Task, TaskError, TaskId}
import repository.TaskRepository
import zio.{Task => ZTask, ZIO, ZLayer}

final case class TaskService(repo: TaskRepository) {

  def getAll: ZTask[List[Task]] = repo.findAll

  def getById(id: TaskId): ZIO[Any, TaskError, Task] =
    repo.findById(id).flatMap {
      case Some(task) => ZIO.succeed(task)
      case None       => ZIO.fail(TaskError.TaskNotFound(id))
    }

  def create(title: String, description: Option[String]): ZIO[Any, TaskError, Task] = {
    if (title.trim.isEmpty) {
      ZIO.fail(TaskError.ValidationError("title", "cannot be empty"))
    } else {
      val task = Task.create(title.trim, description.map(_.trim).filter(_.nonEmpty))
      repo.create(task)
    }
  }

  def update(
    id: TaskId,
    title: String,
    description: Option[String],
    completed: Boolean
  ): ZIO[Any, TaskError, Task] = {
    if (title.trim.isEmpty) {
      ZIO.fail(TaskError.ValidationError("title", "cannot be empty"))
    } else {
      for {
        existing <- getById(id)
        updated = existing.copy(
          title = title.trim,
          description = description.map(_.trim).filter(_.nonEmpty),
          completed = completed
        )
        result <- repo.update(updated)
      } yield result
    }
  }

  def delete(id: TaskId): ZIO[Any, TaskError, Unit] =
    repo.delete(id).flatMap {
      case true  => ZIO.unit
      case false => ZIO.fail(TaskError.TaskNotFound(id))
    }
}

object TaskService {
  val layer: ZLayer[TaskRepository, Nothing, TaskService] =
    ZLayer.fromFunction(TaskService(_))
}
