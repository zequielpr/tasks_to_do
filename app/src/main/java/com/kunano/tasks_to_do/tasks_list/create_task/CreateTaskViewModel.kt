package com.kunano.tasks_to_do.tasks_list.create_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunano.tasks_to_do.core.data.TaskRepository
import com.kunano.tasks_to_do.core.data.model.entities.LocalCategoryEntity
import com.kunano.tasks_to_do.core.data.model.entities.LocalTaskEntity
import com.kunano.tasks_to_do.core.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(private val taskRepository: TaskRepository) :
    ViewModel() {
    //The business logic can be updated from this view model
    private val _createTaskUiState = MutableStateFlow(CreateTaskUiState())

    private val _showIntroduceTaskNameToast = MutableSharedFlow<Boolean>()

    //Read only
    val createTaskUiState: StateFlow<CreateTaskUiState> = _createTaskUiState.asStateFlow()

    val showIntroduceTaskNameToast: SharedFlow<Boolean> = _showIntroduceTaskNameToast.asSharedFlow()


    private var categoriesList = listOf(
        "category 1",
        "Task 2",
        "category 3",
    )

    init {

    }


    fun createTask() {
        //Create task
        val taskName: String = createTaskUiState.value.taskName
        val dueDate: Long = createTaskUiState.value.selectedDateInMilliseconds
        val categoryIdFk: Long? = createTaskUiState.value.selectedCategoryInBottomSheet?.categoryId

        //Clear task name
        if (taskName.isEmpty()) {
            showIntroduceTaskNameToast(true)
            return
        }

        val task = LocalTaskEntity(
            taskTitle = taskName,
            dueDate = Utils.millToLocalDateTime(dueDate),
            categoryIdFk = categoryIdFk,
            isCompleted = false,
        )

        onChangeName("")
        viewModelScope.launch {
            taskRepository.insertTask(task)
        }
    }


    fun showIntroduceTaskNameToast(show: Boolean) {
        viewModelScope.launch {
            _showIntroduceTaskNameToast.emit(show)
        }
    }


    fun hideCategoryDropDownMenu() {
        updateCategoriesDropDownMenuState(show = false)
    }

    fun showCategoryDropDownMenu() {
        updateCategoriesDropDownMenuState(show = true)
    }

    fun hideDatePicker() {
        updateShowDatePickerState(show = false)
    }

    fun showDatePicker() {
        updateShowDatePickerState(show = true)
    }

    fun setDate(date: Long?) {
        hideDatePicker()
        if (date != null) {
            updateSelectedDateInMilliseconds(date = date)

            val dayOfMonth = Utils.millToLocalDateTime(date).dayOfMonth
            updateDayOfMonth(day = dayOfMonth)
        }
    }


    fun selectTaskCategory(category: LocalCategoryEntity?) {
        updateSelectedCategory(category = category)
    }

    fun onChangeName(newValue: String) {
        updateTaskName(name = newValue)
    }


    private fun updateCategoriesDropDownMenuState(show: Boolean) {
        _createTaskUiState.update { currentState ->
            currentState.copy(showCategoriesDropDownMenu = show)
        }
    }

    private fun updateShowDatePickerState(show: Boolean) {
        _createTaskUiState.update { createTaskUiState ->
            createTaskUiState.copy(showDatePicker = show)
        }
    }


    private fun updateSelectedDateInMilliseconds(date: Long) {
        _createTaskUiState.update { createTaskUiState ->
            createTaskUiState.copy(selectedDateInMilliseconds = date)
        }
    }

    private fun updateDayOfMonth(day: Int) {
        _createTaskUiState.update { createTaskUiState ->
            createTaskUiState.copy(selectedDayOfMonth = day)
        }
    }

    private fun updateSelectedCategory(category: LocalCategoryEntity?) {
        _createTaskUiState.update { createTaskUiState ->
            createTaskUiState.copy(selectedCategoryInBottomSheet = category)
        }
    }

    private fun updateTaskName(name: String) {
        _createTaskUiState.update { createTaskUiState ->
            createTaskUiState.copy(taskName = name)
        }
    }


}