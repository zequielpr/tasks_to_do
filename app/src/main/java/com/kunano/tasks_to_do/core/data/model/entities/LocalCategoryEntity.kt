package com.kunano.tasks_to_do.core.data.model.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class LocalCategoryEntity(
    @PrimaryKey(autoGenerate = true) val categoryId: Long? = null,
    val taskQuantity: Int = 0,
    var categoryName: String
)