package com.kunano.tasks_to_do.core.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kunano.tasks_to_do.core.data.model.daos_interfaces.SubTaskDao
import com.kunano.tasks_to_do.core.data.model.daos_interfaces.TaskDao
import com.kunano.tasks_to_do.core.data.model.entities.LocalSubTaskEntity
import com.kunano.tasks_to_do.core.data.model.entities.LocalTaskEntity

@Database(entities = [LocalTaskEntity::class, LocalSubTaskEntity::class], version = 1)
abstract class LocalDataSource: RoomDatabase(){
    abstract fun TaskDao(): TaskDao
    abstract fun SubTaskDao(): SubTaskDao

    companion object{
        private lateinit var db: LocalDataSource

        fun getDataBaseInstance(applicationContext: Context): LocalDataSource{
            if (db != null){
                db = Room.databaseBuilder(
                    applicationContext,
                    LocalDataSource::class.java, "database-name"
                ).build()
            }

            return db
        }
    }
}