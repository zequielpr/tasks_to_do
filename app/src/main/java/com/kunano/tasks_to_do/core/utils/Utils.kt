package com.kunano.tasks_to_do.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class Utils {
    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        fun getCurrentTimeInMilliseconds(): Long{
            val currentLocalDate = LocalDateTime.now()
            val currentLocalDateInMilli = currentLocalDate.atZone(ZoneId.systemDefault())  // Apply the system's default timezone
                .toInstant()// Convert to Instant
                .toEpochMilli();// Get milliseconds since epoch
            return currentLocalDateInMilli
        }

        fun localDateToMilliseconds(localDate: LocalDateTime): Long{
            val currentLocalDateInMilli = localDate.atZone(ZoneId.systemDefault())  // Apply the system's default timezone
                .toInstant()// Convert to Instant
                .toEpochMilli();// Get milliseconds since epoch
            return currentLocalDateInMilli
        }


        fun millToLocalDateTime(timestampInMilli: Long): LocalDateTime{
            val localDateTime = Instant.ofEpochMilli(timestampInMilli)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

            return localDateTime
        }


        fun localDateToString(dateTime: LocalDateTime,  style: FormatStyle): String{
            val dateFormatStyle = DateTimeFormatter.ofLocalizedDateTime(style)
            return dateTime.format(dateFormatStyle)
        }

        fun localDateToString(dateTime: Long, style: FormatStyle): String{
            val dateTime = millToLocalDateTime(dateTime)
            val dateFormatStyle = DateTimeFormatter.ofLocalizedDateTime(style)
           return dateTime.format(dateFormatStyle)
        }
    }
}