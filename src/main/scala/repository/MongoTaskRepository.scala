package repository

import com.github.mongocamp.driver.mongodb._
import domain.{Task, TaskId}
import zio.{Task => ZTask, ZIO, ZLayer}

import java.time.Instant

/**
 * MongoDB implementation using mongo4cats-zio.
 *
 * NOTE: You may need to adjust the exact mongo4cats API calls
 * depending on the final version you use (0.6.x / 0.7.x).
 * This is a solid starting point for a real NoSQL repository.
 */
final class MongoTaskRepository(database: MongoDatabase) extends TaskRepository {

  private val collection: MongoCollection[Task] = database.getCollection[Task]("tasks")

  override def findAll: ZTask[List[Task]] =
    ZIO.fromFuture(_ => collection.find().resultList()).orDie

  override def findById(id: TaskId): ZTask[Option[Task]] =
    ZIO.fromFuture(_ => collection.findOne(Map("_id" -> id.value))).orDie

  override def create(task: Task): ZTask[Task] = {
    val newTask = task.copy(id = TaskId(System.currentTimeMillis()))
    ZIO.fromFuture(_ => collection.insertOne(newTask).map(_ => newTask)).orDie
  }

  override def update(task: Task): ZTask[Task] = {
    val updated = task.copy(updatedAt = Instant.now())
    ZIO.fromFuture(_ => collection.replaceOne(Map("_id" -> task.id.value), updated).map(_ => updated)).orDie
  }

  override def delete(id: TaskId): ZTask[Boolean] =
    ZIO.fromFuture { _ =>
      collection.deleteOne(Map("_id" -> id.value)).map(_.getDeletedCount > 0)
    }.orDie
}

object MongoTaskRepository {

  val layer: ZLayer[MongoConfig, Throwable, TaskRepository] =
    ZLayer.scoped {
      for {
        config <- ZIO.service[MongoConfig]
        client <- MongoClient.fromConnectionString(config.uri)
        db     = client.getDatabase(config.database)
        repo   = new MongoTaskRepository(db)
      } yield repo
    }
}
