// Lab 9
package com.example.routes

import com.example.models.*
import com.example.repositories.InMemoryTaskRepository
import com.example.services.TaskService
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.routing.routing
import io.ktor.server.testing.*
import kotlin.test.*

class TaskRoutesTest {
    private val sharedRepository = InMemoryTaskRepository()
    private fun ApplicationTestBuilder.setupApplication() {
        val service = TaskService(sharedRepository)
        application {
            install(ContentNegotiation) { json() }
            routing { taskRoutes(service) }
        }
    }

    @Test
    fun `GET all tasks should return empty list initially`() = testApplication {
        setupApplication()
        val response = client.get("/api/tasks")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("[]", response.bodyAsText())
    }

    @Test
    fun `POST task should create task and return 201`() = testApplication {
        setupApplication()
        val response = client.post("/api/tasks") {
            contentType(ContentType.Application.Json)
            setBody("""{"title":"Test Task","description":"Test Description","priority":3}""")
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertTrue(response.bodyAsText().contains("Test Task"))
    }

    @Test
    fun `POST task with blank title should return 400`() = testApplication {
        setupApplication()
        val response = client.post("/api/tasks") {
            contentType(ContentType.Application.Json)
            setBody("""{"title":"","description":"Test","priority":3}""")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `GET task by id should return 404 when not found`() = testApplication {
        setupApplication()
        val response = client.get("/api/tasks/999")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `full CRUD lifecycle should work`() = testApplication {
        setupApplication()
        val createResponse = client.post("/api/tasks") {
            contentType(ContentType.Application.Json)
            setBody("""{"title":"Lifecycle Task","description":"Test","priority":3}""")
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)
        val getResponse = client.get("/api/tasks/1")
        assertEquals(HttpStatusCode.OK, getResponse.status)
        assertTrue(getResponse.bodyAsText().contains("Lifecycle Task"))
        val updateResponse = client.put("/api/tasks/1") {
            contentType(ContentType.Application.Json)
            setBody("""{"title":"Updated Task","description":"Updated","priority":5}""")
        }
        assertEquals(HttpStatusCode.OK, updateResponse.status)
        assertTrue(updateResponse.bodyAsText().contains("Updated Task"))
        val deleteResponse = client.delete("/api/tasks/1")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)
        val verifyResponse = client.get("/api/tasks/1")
        assertEquals(HttpStatusCode.NotFound, verifyResponse.status)
    }
}