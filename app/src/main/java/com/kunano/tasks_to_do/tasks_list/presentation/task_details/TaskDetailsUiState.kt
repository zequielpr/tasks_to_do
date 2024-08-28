package com.kunano.tasks_to_do.tasks_list.presentation.task_details

import com.kunano.tasks_to_do.R
import com.kunano.tasks_to_do.core.utils.Utils


data class TaskDetailsUiState(


    val subTasksInputInputState: List<SubTaskInputState> = mutableListOf(),
    val subTaskToBeAdded: Boolean = false,
    val showDueDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val dueDate: Long = Utils.getCurrentTimeInMilliseconds(),
    val category: String? = null,
    val reminder: Reminder? = null,
    val attachedNote: AttachedNote? = null,
    val subTasks: List<String> = listOf(),
    val dropDownMenuOptions: List<Int> = listOf(
        R.string.mark_as_done,
        R.string.duplicate_task,
        R.string.delete
    ),

    )
data class SubTaskInputState(val taskIdFK: String, val subTaskId: String, val subTaskName: String?, val isSubTaskDone: Boolean)

data class AttachedNote(val title: String? = null, val note: String? = null)

data class Reminder(val eventTime: Long? = null, val reminderTime: Long? = null )
