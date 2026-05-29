# ZIO Task API

Complete CRUD REST API built with **Scala 3 + ZIO 2 + ZIO HTTP** featuring two persistence options:

- **In-Memory** (default) → runs immediately, no Docker needed
- **MongoDB** (NoSQL) → real persistence using official MongoDB Scala Driver (requires Docker)

Persistence is controlled by **a single flag** in `Main.scala`.

This project was created for study and portfolio purposes, with clean architecture, typed errors, and well-organized code.

---

## Prerequisites

- Java 11 or higher (you already have it installed)
- sbt (Scala build tool)

### Installing sbt with Coursier (recommended)

```bash
curl -fL https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-linux.gz | gzip -d > cs
chmod +x cs
./cs install sbt
```

Restart your terminal and verify the installation:

```bash
sbt --version
```

---

## ✅ How to Run the Project

### Choosing Persistence (Important)

Persistence is controlled by **a single flag** in `Main.scala`:

```scala
// ============================================
// ESCOLHA A PERSISTÊNCIA AQUI (só mude esta linha)
// ============================================
private val useMongo = false   // true = MongoDB | false = In-Memory
```

- `false` (padrão) → **In-Memory** (rápido, não precisa de Docker)
- `true` → **MongoDB** (persistência real, precisa do Docker)

---

### 1. Running with In-Memory (Recommended for first tests)

Deixe a flag como `false`:

```scala
private val useMongo = false
```

Então rode:

```bash
sbt run
```

A API sobe em: **http://localhost:8080**

Teste rápido:

```bash
curl http://localhost:8080/health
```

---

### 2. Running with MongoDB (Real Persistence)

#### Passos:

1. **Suba o MongoDB** (com Mongo Express):

```bash
docker compose up -d
```

2. **Mude a flag** no `Main.scala`:

```scala
private val useMongo = true
```

3. Rode a aplicação:

```bash
sbt run
```

4. Acesse o Mongo Express (interface web do banco):

- **URL**: http://localhost:8081
- Usuário: `admin`
- Senha: `admin123`

---

## Como voltar para In-Memory

Basta mudar a flag de volta para `false`:

```scala
private val useMongo = false
```

Depois rode `sbt run` novamente.

---

## Available Endpoints

| Method | Route                             | Description                   |
|--------|-----------------------------------|-------------------------------|
| GET    | `/health`                         | Health check                  |
| GET    | `/tasks`                          | List all tasks                |
| GET    | `/tasks?completed=true&limit=10`  | Filter + limit                |
| GET    | `/tasks/:id`                      | Get task by ID                |
| POST   | `/tasks`                          | Create a new task             |
| PUT    | `/tasks/:id`                      | Update a task                 |
| DELETE | `/tasks/:id`                      | Delete a task                 |

### curl Examples

**Create task:**
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Study ZIO + MongoDB","description":"Finish the project"}'
```

**Filter tasks:**
```bash
curl "http://localhost:8080/tasks?completed=false&limit=5"
```

---

## Run Tests

```bash
sbt test
```

---

## Project Structure

```
src/main/scala
├── api/           → HTTP routes
├── config/        → MongoDB configuration
├── domain/        → Models + TaskError (typed errors)
├── repository/    → InMemory + MongoTaskRepository
├── service/       → Business logic
└── Main.scala     → Entry point + persistence selection
```

---

## Notes

- The project is configured with **In-Memory** by default for easy development.
- The **MongoDB** implementation is ready but may require small adjustments when enabling mongo4cats integration.
- To switch back, just follow the steps above.

---

Good luck! When you return, the project is already organized and documented.
