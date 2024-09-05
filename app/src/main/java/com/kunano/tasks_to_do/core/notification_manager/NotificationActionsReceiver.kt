package com.kunano.tasks_to_do.core.notification_manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kunano.tasks_to_do.core.data.TaskRepository
import com.kunano.tasks_to_do.core.reminders_manager.ReminderManager.Companion.TASK_ID
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NotificationActionsReceiver : BroadcastReceiver() {
    companion object{
        const val MARK_TASK_AS_DON: String = "mark_task_as_done"
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        context?.let {

            val action = intent?.action
            val taskId = intent?.getLongExtra(TASK_ID, 0L)

            when(action){
                MARK_TASK_AS_DON ->{markTaskAsDone(taskId, it)}
            }
        }




    }

    private fun markTaskAsDone(taskId: Long?, context: Context){
        val taskRepository = TaskRepository(context)
        GlobalScope.launch {
            taskId?.let {
                taskRepository.markTaskAsDon(it)
            }
        }
    }


}