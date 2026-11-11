// Lab 9
package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Task(val id: Int, val title: String, val description: String = "", val completed: Boolean = false, val priority: Int = 3)

@Serializable
data class CreateTaskRequest(val title: String, val description: String = "", val priority: Int = 3)

data class ValidationError(val field: String, val message: String)

sealed class ServiceResult<out T> {
    data class Success<T>(val data: T) : ServiceResult<T>()
    data class ValidationFailure(val errors: List<ValidationError>) : ServiceResult<Nothing>()
    data class NotFound(val message: String) : ServiceResult<Nothing>()
    data class Error(val message: String) : ServiceResult<Nothing>()
}