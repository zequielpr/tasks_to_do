package com.kunano.tasks_to_do.core.reminders_manager

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.kunano.tasks_to_do.MainActivity
import com.kunano.tasks_to_do.R
import com.kunano.tasks_to_do.core.notification_manager.ActionData
import com.kunano.tasks_to_do.core.notification_manager.CustomNotificationManager
import com.kunano.tasks_to_do.core.notification_manager.NotificationActionsReceiver
import com.kunano.tasks_to_do.core.notification_manager.NotificationActionsReceiver.Companion.MARK_TASK_AS_DON
import com.kunano.tasks_to_do.core.reminders_manager.ReminderManager.Companion.EVENT_DATE
import com.kunano.tasks_to_do.core.reminders_manager.ReminderManager.Companion.TASK_ID
import com.kunano.tasks_to_do.core.reminders_manager.ReminderManager.Companion.TASK_NAME

class AlarmReceiver : BroadcastReceiver() {

    companion object{
        const val OPEN_TASK_ID_REQUEST = 0
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        // This will be triggered when the alarm goes off.

        val taskName = intent?.getStringExtra(TASK_NAME)
        val taskId = intent?.getLongExtra(TASK_ID, 0L)
        val eventTime = intent?.getStringExtra(EVENT_DATE)


        if (taskName != null && context != null && taskId != null) {

            // the task id is used as notification id
            val markTaskAsDoneIntent: Intent =
                Intent(context, NotificationActionsReceiver::class.java).apply {
                    action = MARK_TASK_AS_DON
                    putExtra(TASK_ID, taskId)
                }


            val markTaskAsDoneIntentPendingIntent: PendingIntent = PendingIntent.getBroadcast(
                context,
                taskId.toInt(),
                markTaskAsDoneIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            val deepLinkIntent = Intent(
                Intent.ACTION_VIEW,
                "https://www.example.com/TaskDetails/${R.string.task_details}/$taskId".toUri(),
                context,
                MainActivity::class.java
            ).putExtra(TASK_ID, taskId)

            val deepLinkPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(deepLinkIntent)
                getPendingIntent(OPEN_TASK_ID_REQUEST, PendingIntent.FLAG_IMMUTABLE)
            }


            val actionData: ActionData = ActionData(
                pendingIntent = markTaskAsDoneIntentPendingIntent,
                icon = R.drawable.task_icon,
                label = R.string.mark_as_done
            )

            val notification: Notification =
                CustomNotificationManager.createNotification(
                    textTitle = taskName,
                    textContent = context.getString(R.string.expected_today_at) + " " + eventTime,
                    context = context,
                    actionData = actionData,
                    contentIntentPendingIntent = deepLinkPendingIntent
                )

            CustomNotificationManager.showNotification(
                context = context,
                notificationId = taskId?.toInt() ?: 0,
                notification = notification
            )
        }





        Toast.makeText(context, "Alarm went off!. reminder of $taskName", Toast.LENGTH_SHORT)
            .show();
    }

}
