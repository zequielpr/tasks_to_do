package com.kunano.tasks_to_do.core.data.model.daos_interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kunano.tasks_to_do.core.data.model.entities.LocalTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    //it has a sub query to obtain the quantity of subtasks within a task in realtime
    @Query("SELECT taskId, categoryIdFk, taskTitle, createDateTime," +
            " taskId, dueDate, isCompleted, (select count(subTaskId) from subTaskTable where taskIdFk = taskId) as subTaskQuantity, reminderTime, " +
            "eventTime, title, content FROM TASK_TABLE")
    fun getAll(): Flow<List<LocalTaskEntity>>

    @Query("SELECT * FROM task_table WHERE taskId IN (:taskId)")
    suspend fun getTaskById(taskId: Long): LocalTaskEntity

    @Query("SELECT * FROM task_table WHERE taskId IN (:taskId)")
    fun getTaskByIdLive(taskId: Long): Flow<LocalTaskEntity>

    @Update
    suspend fun updateTask(taskEntity: LocalTaskEntity): Int

    @Insert
    suspend fun insertTask(taskEntity: LocalTaskEntity): Long

    @Delete
    suspend fun deleteTask(taskEntity: LocalTaskEntity): Int
}