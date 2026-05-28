# ZIO Task API

Complete CRUD REST API built with **Scala 3 + ZIO 2 + ZIO HTTP** featuring two persistence options:

- **In-Memory** (default) → runs immediately
- **MongoDB** (NoSQL) → real persistence using mongo4cats

This project was created for study and portfolio purposes, with clean architecture, typed errors, and well-organized code.

---

## ✅ How to Run the Project

### 1. Easy Mode - In-Memory (No Docker)

```bash
cd zio-task-api
sbt run
```

The API will start at: **http://localhost:8080**

Quick test:

```bash
curl http://localhost:8080/health
```

---

### 2. Running with MongoDB (Real Persistence)

#### Steps:

1. **Start MongoDB** (with Mongo Express):

```bash
docker compose up -d
```

2. **Switch persistence in the code**

Open the file:
```
src/main/scala/Main.scala
```

Locate this section and change it as shown below:

```scala
// ============================================
// CHOOSE PERSISTENCE HERE
// ============================================

// 1. In-Memory (default) - works without Docker
private val repositoryLayer: ZLayer[Any, Throwable, TaskRepository] =
  InMemoryTaskRepository.layer

// 2. MongoDB (enable this option)
// private val repositoryLayer: ZLayer[Any, Throwable, TaskRepository] =
//   MongoConfig.layer >>> MongoTaskRepository.layer
```

- Comment out the In-Memory line
- Uncomment the two MongoDB lines

3. Run the application:

```bash
sbt run
```

4. Access Mongo Express (web interface for the database):

- **URL**: http://localhost:8081
- Username: `admin`
- Password: `admin123`

---

## How to Switch Back to In-Memory

Keep only this line active in `Main.scala`:

```scala
private val repositoryLayer = InMemoryTaskRepository.layer
```

Then run `sbt run` again.

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
