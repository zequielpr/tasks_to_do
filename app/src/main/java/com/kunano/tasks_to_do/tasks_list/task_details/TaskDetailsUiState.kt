package com.kunano.tasks_to_do.tasks_list.task_details

import androidx.compose.material3.SnackbarHostState
import com.kunano.tasks_to_do.R
import com.kunano.tasks_to_do.core.data.model.entities.LocalSubTaskEntity
import com.kunano.tasks_to_do.core.utils.Utils


data class TaskDetailsUiState(

    val taskTitle: String = "",
    val subTasksInputInputState: List<LocalSubTaskEntity> = listOf(),
    val showDueDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val dueDate: String? = null,
    val dueDateLong: Long? = null,

    val category: String? = null,
    val reminder: Reminder? = null,
    val attachedNote: AttachedNote? = null,
    val dropDownMenuOptions: List<Int> = listOf(
        R.string.mark_as_done, R.string.duplicate_task, R.string.delete
    ),
    val  snackBarHostState: SnackbarHostState = SnackbarHostState()

    )

data class AttachedNote(val title: String? = null, val note: String? = null)

data class Reminder(val eventTime: String? = null, val reminderTime: String? = null)
