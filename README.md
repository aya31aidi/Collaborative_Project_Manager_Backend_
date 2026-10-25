# Collaborative Project Manager · API

Spring Boot REST API behind the [Angular client](https://github.com/aya31aidi/Collaborative_Project_Manager_Frontend):
people, teams, projects, sprints, tasks and tags, stored in MySQL.

## Run it

**With Docker** (MySQL included):

```bash
cd projectmanager
docker compose up --build
```

The API answers on http://localhost:8070. MySQL is published on port 3307 so it does not clash with a local MySQL.

**Without Docker** (Java 17+, a MySQL or MariaDB on localhost:3306, user `root` without a password, as in XAMPP):

```bash
cd projectmanager
./mvnw spring-boot:run
```

The `projectmanager` database is created on first start. If it is empty, a small demo workspace
(6 people, 2 teams, 2 projects with sprints dated around today) is added so the client has something to show.

| Variable | Default | |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/projectmanager?createDatabaseIfNotExist=true` | JDBC URL |
| `DB_USERNAME` / `DB_PASSWORD` | `root` / empty | database account |
| `CORS_ORIGINS` | `http://localhost:4200` | allowed front-end origins, comma-separated |
| `DEMO_DATA` | `true` | `false` skips the demo workspace |
| `PORT` | `8070` | HTTP port |

## Tests

```bash
./mvnw test
```

The tests run on an in-memory H2 database, so no MySQL is needed. They go through the API the way the client does:
the full team → project → sprint → task flow, renames that must not lose data, deletes that must not hit foreign keys,
validation and error responses.

## Model

```
Team ──< members >── User
 │ 1
 │ 1
Project ──< Sprint ──< Task ──< Tag
                         └── assignedTo: User
```

A team works on one project. Deleting a project removes its sprints, tasks and tags; deleting a team keeps the project
(without a team); deleting a person unassigns their tasks and takes them out of their teams.

## Endpoints

| | |
|---|---|
| `GET/POST /users`, `GET/PUT/DELETE /users/{id}` | people (the password is accepted but never returned) |
| `GET/POST /teams`, `GET/PUT/DELETE /teams/{id}` | teams, a PUT only renames |
| `GET /teams/{id}/users`, `POST /teams/{id}/addUser/{userId}`, `DELETE /teams/{id}/users/{userId}` | team members |
| `GET /projects`, `POST /projects/{teamId}`, `GET/PUT/DELETE /projects/{id}` | projects, created for a team |
| `GET /projects/team/{teamId}`, `GET /projects/{id}/sprints`, `GET /projects/{id}/tasks` | lookups |
| `POST /sprints/{projectId}`, `GET/PUT/DELETE /sprints/{id}`, `GET /sprints/{id}/users` | sprints and the people tasks can go to |
| `GET/POST /tasks`, `GET/PUT/DELETE /tasks/{id}`, `PUT /tasks/{id}/status` | tasks, see below |
| `GET /tags`, `GET /tags/task/{taskId}`, `POST /tags?tag=&taskId=`, `PUT/DELETE /tags/{id}` | tags |

A task is written with ids and tag names:

```json
{
  "title": "Order list screen",
  "description": "Filters by status and date",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "sprintId": 2,
  "assigneeId": 3,
  "tags": ["frontend", "ux"]
}
```

Errors always come back as `{"status": 404, "message": "Sprint 9 was not found"}`, with a `fields` map on validation errors.
