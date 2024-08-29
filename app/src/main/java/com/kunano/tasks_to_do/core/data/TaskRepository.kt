package com.kunano.tasks_to_do.core.data

import android.content.Context
import com.kunano.tasks_to_do.core.data.model.daos_interfaces.TaskDao
import com.kunano.tasks_to_do.core.data.model.entities.LocalTaskEntity
import com.kunano.tasks_to_do.core.data.source.LocalDataSource
import com.kunano.tasks_to_do.core.data.source.LocalDataSource.Companion.getDataBaseInstance
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    @ApplicationContext val context: Context
) {

    var taskDao: TaskDao = getDataBaseInstance(context).TaskDao()


    fun getTask(taskId: String) {

    }

    fun getTasksList() {

    }


    fun insertTask(taskEntity: LocalTaskEntity) {

    }

    fun deleteTask(taskEntity: LocalTaskEntity) {

    }

    fun updateTask(taskEntity: LocalTaskEntity) {

    }

}