package com.kunano.tasks_to_do.core.data.model.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    var dueDate: Long,
    var isCompleted: Boolean,
    @Embedded var note: Note? = null
)


data class Note(var title: String?, var content: String?, var lastModifiedDate: Long?)