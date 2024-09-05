package com.kunano.tasks_to_do.core.reminders_manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.kunano.tasks_to_do.core.data.model.entities.LocalTaskEntity
import com.kunano.tasks_to_do.core.utils.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.format.FormatStyle
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderManager @Inject constructor(@ApplicationContext private val context: Context) {

    private var alarManager: AlarmManager? = null
    private val TAG: String = "ReminderManager"

    companion object {
        const val TASK_NAME = "task_name"
        const val TASK_ID = "task_id"
        const val EVENT_DATE = "event_date"
    }

    init {
        alarManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }


    @RequiresApi(Build.VERSION_CODES.S)
    fun setReminder(taskEntity: LocalTaskEntity) {
        var eventDateString: String = ""
        taskEntity.reminder?.eventTime?.let {
            eventDateString = Utils.localDateToString(it, FormatStyle.SHORT).split(",").last()
        }




        val intent: Intent =
            Intent(context, AlarmReceiver::class.java).apply {
                putExtra(TASK_NAME, taskEntity.taskTitle)
                putExtra(TASK_ID, taskEntity.taskId)
                putExtra(EVENT_DATE, eventDateString)
            }

        //Identify the pending intent with the id of the task
        val alarmIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            taskEntity.taskId?.toInt() ?: 0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        taskEntity?.reminder?.reminderTime?.let {
            val triggerTime = Utils.localDateToMilliseconds(it.withSecond(0))

            //As the pending intent is identified with the task id, each task can have only one reminder at the time
            alarManager?.let { alarManager ->
                if (alarManager.canScheduleExactAlarms()) {
                    try {
                        alarManager?.setExact(AlarmManager.RTC_WAKEUP, triggerTime, alarmIntent)
                    } catch (e: Exception) {
                        Log.e(TAG, "There has been an error. The exact alarm could not be set")
                    }
                } else {
                    Log.e(TAG, "the exact alarm could no be set")
                }
            }


        }
    }
}