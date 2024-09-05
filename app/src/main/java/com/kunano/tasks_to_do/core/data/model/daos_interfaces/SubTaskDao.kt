package com.kunano.tasks_to_do.core.data.model.daos_interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kunano.tasks_to_do.core.data.model.entities.LocalSubTaskEntity
import com.kunano.tasks_to_do.core.data.model.entities.LocalTaskEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface SubTaskDao {
    @Query("SELECT * FROM subTaskTable WHERE taskIdFk in (:taskId)")
    suspend fun getAll(taskId: Long): List<LocalSubTaskEntity>

    @Query("SELECT * FROM subTaskTable WHERE taskIdFk in (:taskId)")
    fun getAllLive(taskId: Long): Flow<List<LocalSubTaskEntity>>

    @Update
    suspend fun updateSubTask(localSubTaskEntity: LocalSubTaskEntity): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubTask(localSubTaskEntity: LocalSubTaskEntity): Long

    @Delete
    suspend fun deleteSubTask(localSubTaskEntity: LocalSubTaskEntity): Int
}