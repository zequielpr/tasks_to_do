package com.kunano.tasks_to_do.core.data.model.daos_interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kunano.tasks_to_do.core.data.model.entities.LocalCategoryEntity
import com.kunano.tasks_to_do.core.data.model.entities.LocalSubTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM localcategoryentity")
    fun getAll(): Flow<List<LocalCategoryEntity>>

    @Query("SELECT * FROM localcategoryentity WHERE categoryId in (:categoryId)")
    suspend fun getCategoryById(categoryId: Long): LocalCategoryEntity

    @Update
    suspend fun updateCategory(categoryEntity: LocalCategoryEntity)

    @Insert
    suspend fun insertCategory(categoryEntity: LocalCategoryEntity): Long

    @Delete
    suspend fun deleteCategory(categoryEntity: LocalCategoryEntity)
}