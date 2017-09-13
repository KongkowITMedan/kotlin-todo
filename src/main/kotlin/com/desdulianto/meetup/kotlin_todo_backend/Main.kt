package com.desdulianto.meetup.kotlin_todo_backend

import com.desdulianto.meetup.kotlin_todo_backend.models.Task
import com.desdulianto.meetup.kotlin_todo_backend.services.TaskService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils
import org.slf4j.LoggerFactory
import spark.Spark.*


fun main(args: Array<String>) {
    val logger = LoggerFactory.getLogger("todo-api")
    val databaseUrl = "jdbc:h2:mem:todo"
    val conn = JdbcConnectionSource(databaseUrl)
    val taskDao: Dao<Task, Int> = DaoManager.createDao(conn, Task::class.java)
    val mapper = jacksonObjectMapper()
    val taskService = TaskService(taskDao)

    logger.info("Starting...")

    logger.info("Create table...")
    TableUtils.createTable(conn, Task::class.java)

    path("/api") {
        before("/*") { _, res ->
            res.type("application/json")
        }

        post("/task") { req, _ ->
            val task = mapper.readValue<Task>(req.body())

            taskService.save(task)

            mapper.writeValueAsString(task)
        }

        // all tasks
        get("/task") { _, _ ->
            val tasks = taskService.findAll()

            mapper.writeValueAsString(tasks)
        }

        // completed tasks
        get("/task/completed") { _, _ ->
            val tasks = taskService.findAllCompleted()

            mapper.writeValueAsString(tasks)
        }

        // active tasks
        get("/task/active") { _, _ ->
            val tasks = taskService.findAllActive()

            mapper.writeValueAsString(tasks)
        }

        // request validation
        before("/task/:id") { req, res ->
            val id = req.params("id").toIntOrNull()
            if (id != null) {
                // don't do this on production system!
                // this will do double work of getting the task from database
                // first it will check if the task exists and then for actually getting the task
                val task = taskService.findById(id)
                if (task == null) {
                    res.status(404)
                    halt(mapper.writeValueAsString(mapOf("message" to "Task $id not found")))
                }
            } else {
                res.status(400)
                halt(mapper.writeValueAsString(mapOf("message" to "Bad Request, invalid id format")))
            }
        }

        // one tasks
        get("/task/:id") { req, _ ->
            val task = taskService.findById(req.params("id").toInt())
            mapper.writeValueAsString(task)
        }

        // modify task
        put("/task/:id") { req, _ ->
            val newTask = mapper.readValue<Task>(req.body())
            val oldTask = taskService.findById(req.params("id").toInt())
            oldTask?.let {
                it.content = newTask.content
                it.complete = newTask.complete
                it.editable = newTask.editable
                taskService.save(it)
                mapper.writeValueAsString(it)
            }
        }

        // delete tasks
        delete("/task/:id") { req, res ->
            val task = taskService.findById(req.params("id").toInt())
            task?.let {
                taskService.delete(task)
                res.status(204)
            }
        }
    }
}
