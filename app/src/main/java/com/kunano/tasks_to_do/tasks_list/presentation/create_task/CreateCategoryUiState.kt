package com.kunano.tasks_to_do.tasks_list.presentation.create_task

data class CreateCategoryUiState(
    val showCreateCategoryDialog: Boolean = false,
    val categoryName: String = "",
    val showErrorMessage: Boolean = false)