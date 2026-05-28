package service

import domain.{TaskError, TaskId}
import repository.InMemoryTaskRepository
import zio.test._
import zio.{Scope, ZIO, ZLayer}

object TaskServiceSpec extends ZIOSpecDefault {

  private val layer = InMemoryTaskRepository.layer >>> TaskService.layer

  def spec = suite("TaskService")(
    test("create task with valid title") {
      for {
        service <- ZIO.service[TaskService]
        task    <- service.create("Study ZIO", Some("Implement full CRUD"))
      } yield assertTrue(
        task.title == "Study ZIO",
        task.description.contains("Implement full CRUD"),
        !task.completed
      )
    },

    test("fail when creating task with empty title") {
      for {
        service <- ZIO.service[TaskService]
        result  <- service.create("", None).exit
      } yield assertTrue(
        result.isFailure,
        result.causeOption.exists(_.contains(TaskError.ValidationError("title", "cannot be empty")))
      )
    },

    test("get task by id") {
      for {
        service <- ZIO.service[TaskService]
        created <- service.create("Test task", None)
        found   <- service.getById(created.id)
      } yield assertTrue(found.id == created.id)
    },

    test("return TaskNotFound when getting non-existing task") {
      for {
        service <- ZIO.service[TaskService]
        result  <- service.getById(TaskId(999)).exit
      } yield assertTrue(result.isFailure)
    },

    test("update task") {
      for {
        service <- ZIO.service[TaskService]
        created <- service.create("Old title", None)
        updated <- service.update(created.id, "New title", Some("Updated"), completed = true)
      } yield assertTrue(
        updated.title == "New title",
        updated.completed
      )
    },

    test("delete task") {
      for {
        service <- ZIO.service[TaskService]
        created <- service.create("To be deleted", None)
        _       <- service.delete(created.id)
        result  <- service.getById(created.id).exit
      } yield assertTrue(result.isFailure)
    }
  ).provideLayerShared(layer)
}
