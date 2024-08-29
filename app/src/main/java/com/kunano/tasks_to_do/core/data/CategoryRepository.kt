package com.kunano.tasks_to_do.core.data

import android.content.Context
import com.kunano.tasks_to_do.core.data.model.daos_interfaces.CategoryDao
import com.kunano.tasks_to_do.core.data.model.entities.LocalCategoryEntity
import com.kunano.tasks_to_do.core.data.source.LocalDataSource.Companion.getDataBaseInstance
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(@ApplicationContext context: Context) {
    private val categoryDao: CategoryDao =  getDataBaseInstance(context)!!.CategoryDao()

    suspend fun insertCategory(categoryEntity: LocalCategoryEntity) {
        return withContext(Dispatchers.IO){
            joinAll()
            categoryDao.insertCategory(categoryEntity)
        }
    }


    fun getAll(): Flow<List<LocalCategoryEntity>>{
        return categoryDao.getAll()
    }


    suspend fun deleteCategory(categoryEntity: LocalCategoryEntity){
       return withContext(Dispatchers.IO){
            categoryDao.deleteCategory(categoryEntity)
        }
    }


    suspend fun updateCategory(categoryEntity: LocalCategoryEntity){
       return withContext(Dispatchers.IO){
            categoryDao.updateCategory(categoryEntity)
        }
    }
}