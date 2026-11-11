// Lab 9, Step 3: Write Integration Tests for Routes (see test dir)
package com.example.routes

import com.example.models.*
import com.example.services.TaskService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.taskRoutes(service: TaskService) {
    route("/api/tasks") {
        get { call.respond(service.getAllTasks()) }
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: run { call.respond(HttpStatusCode.BadRequest); return@get }
            when (val r = service.getTask(id)) {
                is ServiceResult.Success -> call.respond(r.data)
                is ServiceResult.NotFound -> call.respond(HttpStatusCode.NotFound)
                else -> call.respond(HttpStatusCode.InternalServerError)
            }
        }
        post {
            val req = call.receive<CreateTaskRequest>()
            when (val r = service.createTask(req)) {
                is ServiceResult.Success -> call.respond(HttpStatusCode.Created, r.data)
                is ServiceResult.ValidationFailure -> call.respond(HttpStatusCode.BadRequest)
                else -> call.respond(HttpStatusCode.InternalServerError)
            }
        }
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: run { call.respond(HttpStatusCode.BadRequest); return@put }
            val req = call.receive<CreateTaskRequest>()
            when (val r = service.updateTask(id, req)) {
                is ServiceResult.Success -> call.respond(r.data)
                is ServiceResult.NotFound -> call.respond(HttpStatusCode.NotFound)
                else -> call.respond(HttpStatusCode.InternalServerError)
            }
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: run { call.respond(HttpStatusCode.BadRequest); return@delete }
            when (val r = service.deleteTask(id)) {
                is ServiceResult.Success -> call.respond(HttpStatusCode.NoContent)
                is ServiceResult.NotFound -> call.respond(HttpStatusCode.NotFound)
                else -> call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}