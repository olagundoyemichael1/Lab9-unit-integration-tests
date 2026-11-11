// Lab 9
package com.example.repositories

import com.example.models.Task
import com.example.models.CreateTaskRequest

interface TaskRepository {
    fun findAll(): List<Task>
    fun findById(id: Int): Task?
    fun create(title: String, description: String, priority: Int): Task
    fun update(id: Int, title: String, description: String, priority: Int): Task?
    fun delete(id: Int): Boolean
}

class InMemoryTaskRepository : TaskRepository {
    private val tasks = mutableListOf<Task>()
    private var nextId = 1
    override fun findAll(): List<Task> = tasks.toList()
    override fun findById(id: Int): Task? = tasks.find { it.id == id }
    override fun create(title: String, description: String, priority: Int): Task {
        val t = Task(nextId++, title, description, priority = priority); tasks.add(t); return t
    }
    override fun update(id: Int, title: String, description: String, priority: Int): Task? {
        val idx = tasks.indexOfFirst { it.id == id }; if (idx == -1) return null
        val u = tasks[idx].copy(title = title, description = description, priority = priority); tasks[idx] = u; return u
    }
    override fun delete(id: Int): Boolean = tasks.removeIf { it.id == id }
}