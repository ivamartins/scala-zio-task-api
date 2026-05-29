package api

import config.MongoConfig
import repository.{InMemoryTaskRepository, MongoTaskRepository, TaskRepository}
import service.TaskService
import zio.http.Server
import zio.logging.backend.SLF4J
import zio.{Runtime, Scope, ZIO, ZIOAppDefault, ZLayer}

object Main extends ZIOAppDefault {

  override val bootstrap =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  // ============================================
  // ESCOLHA A PERSISTÊNCIA AQUI (só mude esta linha)
  // ============================================
  private val useMongo = true   // true = MongoDB | false = In-Memory

  private val repositoryLayer: ZLayer[Any, Throwable, TaskRepository] =
    if (useMongo) {
      MongoConfig.layer >>> MongoTaskRepository.layer
    } else {
      InMemoryTaskRepository.layer
    }

  private val appLayer = repositoryLayer >>> TaskService.layer

  override def run: ZIO[Scope, Any, Any] =
    (for {
      _       <- ZIO.logInfo("🚀 Starting ZIO Task API...")
      service <- ZIO.service[TaskService]
      _       <- ZIO.logInfo("✅ Server running at http://localhost:8080")
      _       <- ZIO.logInfo("   Health check: http://localhost:8080/health")
      _       <- Server.serve((HealthRoutes.routes ++ TaskRoutes(service)).toHttpApp)
    } yield ())
      .provide(
        Server.defaultWithPort(8080),
        appLayer
      )
}
