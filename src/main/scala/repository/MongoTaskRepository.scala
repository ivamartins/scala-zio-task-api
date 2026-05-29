package repository

import com.mongodb.client.model.Filters
import config.MongoConfig
import domain.{Task, TaskId}
import org.mongodb.scala._
import org.mongodb.scala.bson.Document
import zio.{Task => ZTask, ZIO, ZLayer}

import java.time.Instant

final class MongoTaskRepository(database: MongoDatabase) extends TaskRepository {

  private val collection: MongoCollection[Document] = database.getCollection("tasks")

  private def docToTask(doc: Document): Task = Task(
    id = TaskId(doc.getLong("_id")),
    title = doc.getString("title"),
    description = Option(doc.getString("description")),
    completed = doc.getBoolean("completed"),
    createdAt = Instant.parse(doc.getString("createdAt")),
    updatedAt = Instant.parse(doc.getString("updatedAt"))
  )

  private def taskToDoc(task: Task): Document = Document(
    "_id" -> task.id.value,
    "title" -> task.title,
    "description" -> task.description.orNull,
    "completed" -> task.completed,
    "createdAt" -> task.createdAt.toString,
    "updatedAt" -> task.updatedAt.toString
  )

  override def findAll: ZTask[List[Task]] =
    ZIO.fromFuture { implicit ec =>
      collection.find().toFuture().map(_.map(docToTask).toList)
    }.orDie

  override def findById(id: TaskId): ZTask[Option[Task]] =
    ZIO.fromFuture { implicit ec =>
      collection.find(Filters.eq("_id", id.value)).first().toFuture().map {
        case null => None
        case doc  => Some(docToTask(doc))
      }
    }.orDie

  override def create(task: Task): ZTask[Task] =
    ZIO.fromFuture { implicit ec =>
      collection.insertOne(taskToDoc(task)).toFuture().map(_ => task)
    }.orDie

  override def update(task: Task): ZTask[Task] = {
    val updated = task.copy(updatedAt = Instant.now())
    ZIO.fromFuture { implicit ec =>
      collection.replaceOne(Filters.eq("_id", task.id.value), taskToDoc(updated)).toFuture().map(_ => updated)
    }.orDie
  }

  override def delete(id: TaskId): ZTask[Boolean] =
    ZIO.fromFuture { implicit ec =>
      collection.deleteOne(Filters.eq("_id", id.value)).toFuture().map(_.getDeletedCount > 0)
    }.orDie
}

object MongoTaskRepository {

  val layer: ZLayer[MongoConfig, Throwable, TaskRepository] =
    ZLayer.scoped {
      for {
        config <- ZIO.service[MongoConfig]
        client = MongoClient(config.uri)
        db     = client.getDatabase(config.database)
        _      <- ZIO.addFinalizer(ZIO.succeed(client.close()))
      } yield new MongoTaskRepository(db)
    }
}
