package com.kunano.tasks_to_do.core.notification_manager

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kunano.tasks_to_do.R

val CHANNEL_ID = "chanel1"


data class ActionData(val pendingIntent: PendingIntent, val icon: Int, val label: Int)

class CustomNotificationManager {
    companion object {
        fun createNotificationChannel(context: Context) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is not in the Support Library.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.channel_name)
                val descriptionText = context.getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system.
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }


        fun createNotification(
            textTitle: String,
            textContent: String,
            context: Context,
            actionData: ActionData? = null,
            contentIntentPendingIntent: PendingIntent?,
            autoCancel: Boolean? = null,
        ): Notification {
            var builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.task_icon)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(autoCancel ?: true)
                .setContentIntent(contentIntentPendingIntent)


            //add action to the notification
            actionData?.let {
                builder.addAction(it.icon, context.getString(it.label), it.pendingIntent)
            }

            return builder.build()
        }


        fun showNotification(context: Context, notificationId: Int, notification: Notification) {
            with(NotificationManagerCompat.from(context)) {

                // notificationId is a unique int for each notification that you must define.
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                notify(notificationId, notification)
            }
        }
    }
}