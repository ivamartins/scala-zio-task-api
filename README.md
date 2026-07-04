# ZIO Task API

[![CI](https://github.com/ivamartins/zio-task-api/actions/workflows/ci.yml/badge.svg)](https://github.com/ivamartins/zio-task-api/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> A study/portfolio base for **typed, structured concurrency** in Scala. Demonstrates idiomatic use of ZIO 2 and ZIO HTTP for production-grade backend services.

Complete CRUD REST API built with **Scala 3 + ZIO 2 + ZIO HTTP** featuring two persistence options:

- **In-Memory** (default) — runs immediately, no Docker needed
- **MongoDB** (NoSQL) — real persistence using the official MongoDB Scala Driver (requires Docker)

Persistence is controlled by **a single flag** in `Main.scala`.

This project is part of Code Solutions' study/portfolio material: a clean, well-organized codebase that demonstrates **typed errors**, **dependency injection** (via ZLayers), and **structured concurrency** in Scala 3.

## Why this base

- **Idiomatic Scala 3 + ZIO 2** — the modern stack for typed, structured concurrency
- **Clean architecture** with clear separation of concerns (routes / services / repositories)
- **Typed errors** — no exceptions leaking through layers
- **Two persistence options** — swap with a single flag, no rewrite

## Quick start

**Prerequisites:** Java 11+ and sbt.

```bash
# Install sbt (if not installed)
cs install sbt

# Run with in-memory storage (default)
sbt run

# Or run with MongoDB (requires Docker)
docker compose up -d
MONGO_ENABLED=true sbt run
```

The app will start on `http://localhost:8080`.

## API endpoints

- `GET    /tasks` — list all tasks
- `POST   /tasks` — create a task
- `GET    /tasks/:id` — get a task
- `PUT    /tasks/:id` — update a task
- `DELETE /tasks/:id` — delete a task

## Run the tests

```bash
sbt test
```

> **Português?** Veja [`README.pt-BR.md`](./README.pt-BR.md).

## See also

- **Other Code Solutions bases**: [akka-scala-base](https://github.com/ivamartins/akka-scala-base), [flink-kafka-scala-base](https://github.com/ivamartins/flink-kafka-scala-base)
- **Code Solutions**: [ivamartins.github.io/code-solutions-site](https://ivamartins.github.io/code-solutions-site/)
- **All Code Solutions open source**: [github.com/ivamartins](https://github.com/ivamartins)

## License

MIT — see `LICENSE`.
