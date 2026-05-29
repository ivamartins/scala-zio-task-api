package config

// =============================================
// DESABILITADO TEMPORARIAMENTE
// 
// O carregamento via zio-config HOCON está com problemas na versão 4.0.2.
// 
// A escolha entre In-Memory e Mongo agora é feita por uma flag simples
// dentro de Main.scala (useMongo = true/false).
// 
// Este arquivo pode ser removido ou reativado depois.
// =============================================

// import zio._
// import zio.config._
// import zio.config.magnolia._

// final case class RepositoryConfig(`type`: String)

// object RepositoryConfig {
//   ...
// }