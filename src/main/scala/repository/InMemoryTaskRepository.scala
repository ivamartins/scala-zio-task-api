package repository

import domain.{Task, TaskId}
import zio.{Ref, Task => ZTask, ZLayer}

import java.time.Instant

final case class InMemoryTaskRepository(ref: Ref[List[Task]]) extends TaskRepository {

  private def nextId(tasks: List[Task]): Long =
    if (tasks.isEmpty) 1L else tasks.map(_.id.value).max + 1

  override def findAll: ZTask[List[Task]] =
    ref.get

  override def findById(id: TaskId): ZTask[Option[Task]] =
    ref.get.map(_.find(_.id == id))

  override def create(task: Task): ZTask[Task] =
    ref.modify { tasks =>
      val newTask = task.copy(id = TaskId(nextId(tasks)))
      (newTask, newTask :: tasks)
    }

  override def update(task: Task): ZTask[Task] =
    ref.modify { tasks =>
      val updated = task.copy(updatedAt = Instant.now())
      val newList = tasks.map(t => if (t.id == task.id) updated else t)
      (updated, newList)
    }

  override def delete(id: TaskId): ZTask[Boolean] =
    ref.modify { tasks =>
      val exists = tasks.exists(_.id == id)
      val newList = tasks.filterNot(_.id == id)
      (exists, newList)
    }
}

object InMemoryTaskRepository {
  val layer: ZLayer[Any, Nothing, TaskRepository] =
    ZLayer.fromZIO(
      Ref.make(List.empty[Task]).map(InMemoryTaskRepository(_))
    )
}
