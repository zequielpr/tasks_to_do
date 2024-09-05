package com.kunano.tasks_to_do.core.data

import androidx.room.TypeConverter
import com.kunano.tasks_to_do.core.utils.Utils
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromTimeSpam(value: Long?): LocalDateTime?{
        return value?.let { Utils.millToLocalDateTime(it) }
    }

    @TypeConverter
    fun timeSpamToLocalDateTime(dateTime: LocalDateTime?): Long?{
        return dateTime?.let { Utils.localDateToMilliseconds(it) }
    }
}