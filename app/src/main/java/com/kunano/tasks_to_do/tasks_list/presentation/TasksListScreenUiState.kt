package com.kunano.tasks_to_do.tasks_list.presentation

import androidx.annotation.StringRes
import com.kunano.tasks_to_do.R

data class TasksListScreenUiState(
    val sortByDialogData: SortByDialogData = SortByDialogData(),
    val searchingString: String = "",
    val showSortByDialog: Boolean = false,
    val isSearchModeActive: Boolean = false,
    val showCreateTaskDialog: Boolean = false,
    val selectedCategory: String? = null,
    val categoryList: List<String> = listOf(),
    val tasksList: List<String> = listOf()
)

data class SortByDialogData(
    @StringRes val title: Int = R.string.sort_tasks_by,
    @StringRes val selectedOption: Int = R.string.due_date_and_time,
    @StringRes val options: List<Int> = listOf(
        R.string.due_date_and_time,
        R.string.task_create_time_latest_at_the_bottom,
        R.string.task_create_time_latest_at_the_top,
        R.string.alphabetical_a_z, R.string.alphabetical_z_a
    )
)
