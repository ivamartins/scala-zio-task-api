package repository

import domain.{Task, TaskId}
import zio.{Task => ZTask}

trait TaskRepository {
  def findAll: ZTask[List[Task]]
  def findById(id: TaskId): ZTask[Option[Task]]
  def create(task: Task): ZTask[Task]
  def update(task: Task): ZTask[Task]
  def delete(id: TaskId): ZTask[Boolean]
}
