package com.kunano.tasks_to_do.tasks_list.task_details

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TimePickerState
import com.kunano.tasks_to_do.R
import com.kunano.tasks_to_do.core.data.model.entities.LocalSubTaskEntity


data class TaskDetailsUiState @OptIn(ExperimentalMaterial3Api::class) constructor(

    val taskTitle: String = "",
    val subTasksInputInputState: List<LocalSubTaskEntity> = listOf(),
    val showDueDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val dueDate: String? = null,
    val dueDateLong: Long? = null,
    val eventTimePickerState: TimePickerState = TimePickerState(
        0,
        0,
        true
    ),
    val reminderTimePickerState: TimePickerState = TimePickerState(
        0,
        0,
        true
    ),
    val showReminderTimePicker: Boolean = false,

    val category: String? = null,
    val reminderUiState: ReminderUiState = ReminderUiState(),
    val attachedNote: AttachedNote = AttachedNote(),
    val dropDownMenuOptions: List<Int> = listOf(
        R.string.mark_as_done, R.string.duplicate_task, R.string.delete
    ),
    val  snackBarHostState: SnackbarHostState = SnackbarHostState()

    )

data class AttachedNote(val title: String? = null, val note: String? = null)

data class ReminderUiState(val eventTime: String? = null, val reminderTime: String? = null)
