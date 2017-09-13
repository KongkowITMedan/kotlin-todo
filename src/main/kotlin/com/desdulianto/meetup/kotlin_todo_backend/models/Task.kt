package com.desdulianto.meetup.kotlin_todo_backend.models

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "task")
class Task(@DatabaseField(generatedId = true) val id: Int = 0,
           @DatabaseField(canBeNull = false, columnName = "content") var content: String = "",
           @DatabaseField(defaultValue = "false", columnName = "editable") var editable: Boolean = false,
           @DatabaseField(defaultValue = "false", columnName = "complete") var complete: Boolean = false)