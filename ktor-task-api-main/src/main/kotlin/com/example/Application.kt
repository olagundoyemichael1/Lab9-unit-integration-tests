// Lab 9
package com.example

import com.example.repositories.InMemoryTaskRepository
import com.example.routes.taskRoutes
import com.example.services.TaskService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*

fun Application.module() {
    install(ContentNegotiation) { json() }
    val repo = InMemoryTaskRepository()
    val service = TaskService(repo)
    routing { taskRoutes(service) }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") { module() }.start(wait = true)
}