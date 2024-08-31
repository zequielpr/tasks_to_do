package com.kunano.tasks_to_do.tasks_list.create_task

import com.kunano.tasks_to_do.core.data.model.entities.LocalCategoryEntity
import com.kunano.tasks_to_do.core.utils.Utils
import java.time.LocalDateTime

data class CreateTaskUiState(
    val showCreateCategoryDialog: Boolean = false,
    val showDatePicker: Boolean = false,
    val showCategoriesDropDownMenu: Boolean = false,
    val selectedDateInMilliseconds: Long = Utils.getCurrentTimeInMilliseconds(),
    val selectedDayOfMonth: Int = LocalDateTime.now().dayOfMonth,
    val selectedCategoryInBottomSheet: LocalCategoryEntity? = null,
    val taskName: String = "",
)