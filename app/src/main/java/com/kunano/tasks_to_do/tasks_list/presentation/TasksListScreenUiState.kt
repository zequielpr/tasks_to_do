package com.kunano.tasks_to_do.tasks_list.presentation

data class TasksListScreenUiState(
    val searchingString: String = "",
    val isSearchModeActive: Boolean = false,
    val showCreateTaskDialog: Boolean = false,
    val selectedCategory: String? = null,
    val categoryList: List<String> = listOf(),
    val tasksList: List<String> = listOf()
)
