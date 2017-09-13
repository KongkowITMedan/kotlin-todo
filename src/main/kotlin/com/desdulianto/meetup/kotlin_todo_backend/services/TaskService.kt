package com.desdulianto.meetup.kotlin_todo_backend.services

import com.desdulianto.meetup.kotlin_todo_backend.models.Task
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.stmt.QueryBuilder

class TaskService(val dao: Dao<Task, Int>) {

    fun save(task: Task) = dao.createOrUpdate(task)
    fun findById(id: Int): Task? = dao.queryForId(id)
    fun findAll(): List<Task> = dao.queryForAll()
    fun findAllCompleted(): List<Task> = dao.queryForEq("complete", true)
    fun findAllActive(): List<Task> = dao.queryForEq("complete", false)
    fun delete(task: Task) = dao.delete(task)

}