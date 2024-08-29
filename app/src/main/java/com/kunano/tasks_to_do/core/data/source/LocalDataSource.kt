package com.kunano.tasks_to_do.core.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kunano.tasks_to_do.core.data.model.daos_interfaces.CategoryDao
import com.kunano.tasks_to_do.core.data.model.daos_interfaces.SubTaskDao
import com.kunano.tasks_to_do.core.data.model.daos_interfaces.TaskDao
import com.kunano.tasks_to_do.core.data.model.entities.LocalCategoryEntity
import com.kunano.tasks_to_do.core.data.model.entities.LocalSubTaskEntity
import com.kunano.tasks_to_do.core.data.model.entities.LocalTaskEntity

@Database(
    entities = [LocalTaskEntity::class, LocalSubTaskEntity::class, LocalCategoryEntity::class],
    version = 1
)
abstract class LocalDataSource : RoomDatabase() {
    abstract fun TaskDao(): TaskDao
    abstract fun SubTaskDao(): SubTaskDao
    abstract fun CategoryDao(): CategoryDao

    companion object {
        private  var db: LocalDataSource? = null

        fun getDataBaseInstance(applicationContext: Context): LocalDataSource? {
            if (db == null) {
                db = Room.databaseBuilder(
                    applicationContext,
                    LocalDataSource::class.java, "database-name"
                ).build()
            }

            return db
        }
    }
}