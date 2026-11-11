// Lab 9
package com.example.services

import com.example.models.*
import com.example.repositories.TaskRepository
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.*

class TaskServiceTest {
    private lateinit var repository: TaskRepository
    private lateinit var service: TaskService

    @BeforeEach
    fun setup() {
        repository = mockk()
        service = TaskService(repository)
    }

    @Test
    fun `createTask should return success when input is valid`() {
        val request = CreateTaskRequest("Learn Testing", "Write tests", 3)
        val expectedTask = Task(1, "Learn Testing", "Write tests", priority = 3)
        every { repository.create(any(), any(), any()) } returns expectedTask
        val result = service.createTask(request)
        assertTrue(result is ServiceResult.Success)
        assertEquals(expectedTask, (result as ServiceResult.Success).data)
        verify(exactly = 1) { repository.create("Learn Testing", "Write tests", 3) }
    }

    @Test
    fun `createTask should return validation error when title is blank`() {
        val request = CreateTaskRequest("", "Description", 3)
        val result = service.createTask(request)
        assertTrue(result is ServiceResult.ValidationFailure)
        val errors = (result as ServiceResult.ValidationFailure).errors
        assertEquals(1, errors.size)
        assertEquals("title", errors[0].field)
        verify(exactly = 0) { repository.create(any(), any(), any()) }
    }

    @Test
    fun `createTask should return validation error when priority is out of range`() {
        val request = CreateTaskRequest("Valid Title", "Description", 10)
        val result = service.createTask(request)
        assertTrue(result is ServiceResult.ValidationFailure)
        val errors = (result as ServiceResult.ValidationFailure).errors
        assertTrue(errors.any { it.field == "priority" })
    }

    @Test
    fun `getTask should return success when task exists`() {
        val task = Task(1, "Existing Task", "Description")
        every { repository.findById(1) } returns task
        val result = service.getTask(1)
        assertTrue(result is ServiceResult.Success)
        assertEquals(task, (result as ServiceResult.Success).data)
    }

    @Test
    fun `getTask should return not found when task does not exist`() {
        every { repository.findById(999) } returns null
        val result = service.getTask(999)
        assertTrue(result is ServiceResult.NotFound)
    }

    @Test
    fun `deleteTask should return not found when task does not exist`() {
        every { repository.delete(999) } returns false
        val result = service.deleteTask(999)
        assertTrue(result is ServiceResult.NotFound)
    }

    @Test
    fun `deleteTask should return success when task is deleted`() {
        every { repository.delete(1) } returns true
        val result = service.deleteTask(1)
        assertTrue(result is ServiceResult.Success)
    }
}