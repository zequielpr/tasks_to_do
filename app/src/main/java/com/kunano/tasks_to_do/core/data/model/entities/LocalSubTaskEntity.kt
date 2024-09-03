package com.kunano.tasks_to_do.core.data.model.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(
    primaryKeys = ["subTaskId", "taskIdFk" ],
    tableName = "subTaskTable",
    foreignKeys = [ForeignKey(
        entity = LocalTaskEntity::class,
        parentColumns = ["taskId"],
        childColumns = ["taskIdFk"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LocalSubTaskEntity(
    var subTaskId: String,
    val title: String,
    var taskIdFk: Long,
    val isCompleted: Boolean
)