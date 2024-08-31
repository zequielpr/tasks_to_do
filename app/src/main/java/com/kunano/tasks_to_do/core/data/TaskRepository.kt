package com.kunano.tasks_to_do.core.data

import android.content.Context
import com.kunano.tasks_to_do.core.data.model.daos_interfaces.TaskDao
import com.kunano.tasks_to_do.core.data.model.entities.LocalTaskEntity
import com.kunano.tasks_to_do.core.data.source.LocalDataSource.Companion.getDataBaseInstance
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    @ApplicationContext val context: Context
) {

    private var _taskDao: TaskDao = getDataBaseInstance(context)!!.TaskDao()


    //Move the coroutine to the IO thread
    suspend fun getTask(taskId: Long): LocalTaskEntity {
        return _taskDao.getTaskById(taskId = taskId)

    }

    fun getTasksList(): Flow<List<LocalTaskEntity>> {
        return _taskDao.getAll()
    }


    suspend fun insertTask(taskEntity: LocalTaskEntity): Boolean {
        return withContext(Dispatchers.IO) {
            _taskDao.insertTask(taskEntity) > 0
        }
    }

    suspend fun deleteTask(taskEntity: LocalTaskEntity) {
        withContext(Dispatchers.IO){
            _taskDao.deleteTask(taskEntity)
        }
    }

    suspend fun updateTask(taskEntity: LocalTaskEntity) {
        withContext(Dispatchers.IO){
            _taskDao.updateTask(taskEntity)
        }
    }

}