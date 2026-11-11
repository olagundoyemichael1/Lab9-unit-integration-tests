// Lab 9, Step 2: Write Unit Tests for the Service (see test dir)
package com.example.services

import com.example.models.*
import com.example.repositories.TaskRepository

class TaskService(private val repository: TaskRepository) {
    fun createTask(request: CreateTaskRequest): ServiceResult<Task> {
        val errors = mutableListOf<ValidationError>()
        if (request.title.isBlank()) errors.add(ValidationError("title", "Title cannot be blank"))
        if (request.title.length > 100) errors.add(ValidationError("title", "Title cannot exceed 100 characters"))
        if (request.description.length > 1000) errors.add(ValidationError("description", "Description cannot exceed 1000 characters"))
        if (request.priority < 1 || request.priority > 5) errors.add(ValidationError("priority", "Priority must be between 1 and 5"))
        if (errors.isNotEmpty()) return ServiceResult.ValidationFailure(errors)
        return ServiceResult.Success(repository.create(request.title, request.description, request.priority))
    }
    fun updateTask(id: Int, request: CreateTaskRequest): ServiceResult<Task> {
        val errors = mutableListOf<ValidationError>()
        if (request.title.isBlank()) errors.add(ValidationError("title", "Title cannot be blank"))
        if (request.title.length > 100) errors.add(ValidationError("title", "Title cannot exceed 100 characters"))
        if (request.description.length > 1000) errors.add(ValidationError("description", "Description cannot exceed 1000 characters"))
        if (request.priority < 1 || request.priority > 5) errors.add(ValidationError("priority", "Priority must be between 1 and 5"))
        if (errors.isNotEmpty()) return ServiceResult.ValidationFailure(errors)
        val updated = repository.update(id, request.title, request.description, request.priority)
        return if (updated != null) ServiceResult.Success(updated) else ServiceResult.NotFound("Task with id $id not found")
    }

    fun getTask(id: Int): ServiceResult<Task> {
        val t = repository.findById(id)
        return if (t != null) ServiceResult.Success(t) else ServiceResult.NotFound("Task with id $id not found")
    }
    fun getAllTasks(): List<Task> = repository.findAll()
    fun deleteTask(id: Int): ServiceResult<Unit> =
        if (repository.delete(id)) ServiceResult.Success(Unit) else ServiceResult.NotFound("Task with id $id not found")
}