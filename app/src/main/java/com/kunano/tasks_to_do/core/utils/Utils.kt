package com.kunano.tasks_to_do.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class Utils {
    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        fun getCurrentTimeInMilliseconds(): Long{
            val currentLocalDate = LocalDateTime.now()
            val currentLocalDateInMilli = currentLocalDate.toInstant(ZoneOffset.UTC).toEpochMilli()
            return currentLocalDateInMilli
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun millToLocalDateTie(timestampInMilli: Long): LocalDateTime{
            val localDateTime = Instant.ofEpochMilli(timestampInMilli)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

            return localDateTime
        }
    }
}