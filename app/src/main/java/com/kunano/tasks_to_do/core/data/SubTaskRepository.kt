package com.kunano.tasks_to_do.core.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.kunano.tasks_to_do.core.data.model.daos_interfaces.SubTaskDao
import com.kunano.tasks_to_do.core.data.model.entities.LocalSubTaskEntity
import com.kunano.tasks_to_do.core.data.source.LocalDataSource.Companion.getDataBaseInstance
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.Future
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubTaskRepository @Inject constructor(@ApplicationContext val context: Context) {
    private val subTaskDao: SubTaskDao = getDataBaseInstance(context)!!.SubTaskDao()


    suspend fun insertSubTask(subTaskEntity: LocalSubTaskEntity): Boolean{
        return withContext(Dispatchers.IO){
            subTaskDao.insertSubTask(subTaskEntity) > 0
        }
    }

    suspend fun deleteSubTask(subTaskEntity: LocalSubTaskEntity){
        withContext(Dispatchers.IO){
            subTaskDao.deleteSubTask(subTaskEntity)
        }
    }

    suspend fun updateSubTask(subTaskEntity: LocalSubTaskEntity){

        withContext(Dispatchers.IO){
            subTaskDao.updateSubTask(subTaskEntity)
        }
    }

    suspend fun getSubTaskLIst(taskId: Long): List<LocalSubTaskEntity> {
        return withContext(Dispatchers.IO){
            subTaskDao.getAll(taskId = taskId)
        }
    }

}