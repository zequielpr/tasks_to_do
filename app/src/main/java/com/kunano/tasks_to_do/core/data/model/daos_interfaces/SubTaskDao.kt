package com.kunano.tasks_to_do.core.data.model.daos_interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kunano.tasks_to_do.core.data.model.entities.LocalSubTaskEntity
import com.kunano.tasks_to_do.core.data.model.entities.LocalTaskEntity

@Dao
interface SubTaskDao {
    @Query("SELECT * FROM subTaskTable WHERE taskIdFk in (:taskId)")
    suspend fun getAll(taskId: Int): List<LocalSubTaskEntity>

    @Update
    suspend fun updateSubTask(localSubTaskEntity: LocalSubTaskEntity)

    @Insert
    suspend fun insertSubTask(localSubTaskEntity: LocalSubTaskEntity)

    @Delete
    suspend fun deleteSubTask(localSubTaskEntity: LocalSubTaskEntity)
}