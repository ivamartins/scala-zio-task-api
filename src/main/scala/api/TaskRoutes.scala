package api

import domain.{Task, TaskError, TaskId}
import service.TaskService
import zio.http._
import zio.json._
import zio.{ZIO, Task => ZTask}

final case class CreateTaskRequest(title: String, description: Option[String])
final case class UpdateTaskRequest(title: String, description: Option[String], completed: Boolean)

object CreateTaskRequest {
  implicit val decoder: JsonDecoder[CreateTaskRequest] = DeriveJsonDecoder.gen[CreateTaskRequest]
}

object UpdateTaskRequest {
  implicit val decoder: JsonDecoder[UpdateTaskRequest] = DeriveJsonDecoder.gen[UpdateTaskRequest]
}

object TaskRoutes {

  private def errorResponse(error: TaskError): Response = error match {
    case _: TaskError.TaskNotFound    => Response.text(error.message).status(Status.NotFound)
    case _: TaskError.ValidationError => Response.text(error.message).status(Status.BadRequest)
  }

  def apply(service: TaskService): Routes[Any, Response] = Routes(

    // GET /tasks?completed=true&limit=20
    Method.GET / "tasks" ->
      handler { (req: Request) =>
        val completedFilter = req.url.queryParams.get("completed").flatMap(_.headOption).map(_.toBooleanOption).flatten
        val limit           = req.url.queryParams.get("limit").flatMap(_.headOption).flatMap(_.toIntOption).getOrElse(50)

        service.getAll.map { tasks =>
          val filtered = completedFilter match {
            case Some(value) => tasks.filter(_.completed == value)
            case None        => tasks
          }
          val result = filtered.take(limit)
          Response.json(result.toJson)
        }.orDie
      },

    // GET /tasks/:id
    Method.GET / "tasks" / long("id") ->
      handler { (id: Long, _: Request) =>
        service.getById(TaskId(id)).map { task =>
          Response.json(task.toJson)
        }.catchAll(errorResponse)
      },

    // POST /tasks
    Method.POST / "tasks" ->
      handler { (req: Request) =>
        (for {
          body    <- req.body.asString.orDie
          request <- ZIO.fromEither(body.fromJson[CreateTaskRequest])
                      .mapError(e => TaskError.ValidationError("body", e))
          task    <- service.create(request.title, request.description)
        } yield Response.json(task.toJson).status(Status.Created))
          .catchAll(errorResponse)
      },

    // PUT /tasks/:id
    Method.PUT / "tasks" / long("id") ->
      handler { (id: Long, req: Request) =>
        (for {
          body    <- req.body.asString.orDie
          request <- ZIO.fromEither(body.fromJson[UpdateTaskRequest])
                      .mapError(e => TaskError.ValidationError("body", e))
          task    <- service.update(TaskId(id), request.title, request.description, request.completed)
        } yield Response.json(task.toJson))
          .catchAll(errorResponse)
      },

    // DELETE /tasks/:id
    Method.DELETE / "tasks" / long("id") ->
      handler { (id: Long, _: Request) =>
        service.delete(TaskId(id)).as(Response.status(Status.NoContent))
          .catchAll(errorResponse)
      }
  )
}
