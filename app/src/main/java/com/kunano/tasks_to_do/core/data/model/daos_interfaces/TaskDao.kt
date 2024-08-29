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

    @Query("SELECT * FROM TASK_TABLE")
    fun getAll(): Flow<List<LocalTaskEntity>>

    @Query("SELECT * FROM task_table WHERE taskId IN (:taskId)")
    fun getTaskById(taskId: Int): Flow<LocalTaskEntity>

    @Update
    suspend fun updateTask(taskEntity: LocalTaskEntity)

    @Insert
    suspend fun insertTask(taskEntity: LocalTaskEntity)

    @Delete
    suspend fun deleteTask(taskEntity: LocalTaskEntity)
}