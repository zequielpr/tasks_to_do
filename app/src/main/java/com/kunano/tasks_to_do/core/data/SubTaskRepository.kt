package com.kunano.tasks_to_do.core.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.kunano.tasks_to_do.core.data.model.daos_interfaces.SubTaskDao
import com.kunano.tasks_to_do.core.data.model.entities.LocalSubTaskEntity
import com.kunano.tasks_to_do.core.data.source.LocalDataSource.Companion.getDataBaseInstance
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.Future
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubTaskRepository @Inject constructor(@ApplicationContext val context: Context) {
    val subTaskDao: SubTaskDao = getDataBaseInstance(context).SubTaskDao()


    fun insertSubTask(subTaskEntity: LocalSubTaskEntity){

    }

    fun deleteSubTask(subTaskEntity: LocalSubTaskEntity){

    }

    fun updateSubTask(subTaskEntity: LocalSubTaskEntity){

    }

    fun getSubTaskLIst(taskId: String) {

    }

}