package com.kunano.tasks_to_do.core.data.model.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "task_table",
    foreignKeys = [ForeignKey(
        entity = LocalCategoryEntity::class,
        parentColumns = ["categoryId"],
        childColumns = ["categoryIdFk"],
        onDelete = ForeignKey.CASCADE,
    )]
)
data class LocalTaskEntity(
    @PrimaryKey(autoGenerate = true) var taskId: Long? = null,
    var categoryIdFk: Long?,
    var taskTitle: String,
    var dueDate: LocalDateTime,
    var isCompleted: Boolean,
    val subTaskQuantity: Int = 0,
    @Embedded val reminder: Reminder? = null,
    @Embedded var note: Note = Note(),
    val createDateTime: LocalDateTime
)

data class Reminder(val eventTime: LocalDateTime? = null,
                    var reminderTime: LocalDateTime? = null)



data class Note(var title: String? = null, var content: String? = null, var lastModifiedDate: LocalDateTime? = null)