package com.kunano.tasks_to_do.core.data.model.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class LocalTaskEntity(
    @PrimaryKey var taskId: String,
    var taskTitle: String,
    var dueDate: String,
    var isCompleted: Boolean,
    @Embedded var note: Note
)


data class Note(var title: String?, var content: String?, var lastModifiedDate: Long?)