package com.kunano.tasks_to_do.tasks_list.presentation.states

data class CreateCategoryUiState(
    val showCreateCategoryDialog: Boolean = false,
    val categoryName: String = "",
    val showErrorMessage: Boolean = false)