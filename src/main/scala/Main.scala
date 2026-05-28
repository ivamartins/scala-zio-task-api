package api

import config.MongoConfig
import repository.{InMemoryTaskRepository, MongoTaskRepository, TaskRepository}
import service.TaskService
import zio.http.{Server, ServerConfig}
import zio.logging.backend.SLF4J
import zio.{Runtime, Scope, ZIO, ZIOAppDefault, ZLayer}

object Main extends ZIOAppDefault {

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  private val serverConfigLayer =
    ZLayer.succeed(ServerConfig.default.port(8080))

  private val serverLayer =
    Server.live

  // ============================================
  // CHOOSE PERSISTENCE HERE
  // ============================================

  // 1. In-Memory (default) - works without Docker
  private val repositoryLayer: ZLayer[Any, Throwable, TaskRepository] =
    InMemoryTaskRepository.layer

  // 2. MongoDB (uncomment the lines below and comment the one above)
  // private val repositoryLayer: ZLayer[Any, Throwable, TaskRepository] =
  //   MongoConfig.layer >>> MongoTaskRepository.layer

  // ============================================

  private val appLayer = repositoryLayer >>> TaskService.layer

  override def run: ZIO[Scope, Any, Any] = {
    val program = for {
      _ <- ZIO.logInfo("🚀 Starting ZIO Task API...")
      service <- ZIO.service[TaskService]
      httpApp  = HealthRoutes.routes ++ TaskRoutes(service)
      _       <- ZIO.logInfo("✅ Server running at http://localhost:8080")
      _       <- ZIO.logInfo("   Health check: http://localhost:8080/health")
      _       <- Server.serve(httpApp)
    } yield ()

    program.provide(
      serverConfigLayer,
      serverLayer,
      appLayer
    )
  }
}
