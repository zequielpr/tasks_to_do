package com.kunano.tasks_to_do.tasks_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunano.tasks_to_do.R
import com.kunano.tasks_to_do.core.data.CategoryRepository
import com.kunano.tasks_to_do.core.data.SubTaskRepository
import com.kunano.tasks_to_do.core.data.TaskRepository
import com.kunano.tasks_to_do.core.data.model.entities.LocalCategoryEntity
import com.kunano.tasks_to_do.core.data.model.entities.LocalTaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository,
    private val subTaskRepository: SubTaskRepository
) : ViewModel() {

    //Intern updates
    private val _tasksListScreenUiState = MutableStateFlow(TasksListScreenUiState())

    //Read only
    val tasksListScreedUiState = _tasksListScreenUiState


    private var currentTaskList: List<LocalTaskEntity> = listOf()

    init {

        fetchTasksList()
        fetchCategoryList()
    }


    private fun fetchCategoryList() {
        viewModelScope.launch {
            categoryRepository.getAllLive().collect {
                updateCategoriesList(it)
            }
        }
    }


    private fun fetchTasksList() {

        viewModelScope.launch {
            taskRepository.getTasksList().collect {
                println("Collecting...")
                val selectedCategory = _tasksListScreenUiState.value.selectedCategory
                currentTaskList = it
                filterByTaskCategory(selectedCategory)
            }

        }
    }


    fun updateTaskState(task: LocalTaskEntity, isComplete: Boolean) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(isCompleted = isComplete))
        }
    }


    fun checkIfItHasSubTasks(task: LocalTaskEntity) {

    }


    fun selectSortByOption(option: Int) {

        when (option) {
            R.string.due_date_and_time -> {
                sortTaskByDueDateAndTime()
            }

            R.string.task_create_time_latest_at_the_top -> {
                sortTasksByCreateTimeLatestTop()
            }

            R.string.task_create_time_latest_at_the_bottom -> {
                sortTasksByCreateTimeLatestBottom()
            }

            R.string.alphabetical_a_z -> {
                sortTasksByAlphaAZ()
            }

            R.string.alphabetical_z_a -> {
                sortTasksByAlphaZA()
            }
        }

        updateSortBySelectedOption(option = option)
        hideSortByDialog()
    }


    private var sortedList: List<LocalTaskEntity> = listOf()

    private fun sortTaskByDueDateAndTime() {
        sortedList =
            _tasksListScreenUiState.value.tasksList.sortedByDescending { task -> task.dueDate }
                .reversed()
        updateTasksList(sortedList)
    }

    private fun sortTasksByCreateTimeLatestBottom() {
        sortedList =
            _tasksListScreenUiState.value.tasksList.sortedByDescending { task -> task.createDateTime }
                .reversed()
        updateTasksList(sortedList)
    }

    private fun sortTasksByCreateTimeLatestTop() {
        sortedList =
            _tasksListScreenUiState.value.tasksList.sortedByDescending { task -> task.createDateTime }
        updateTasksList(sortedList)
    }

    private fun sortTasksByAlphaAZ() {
        sortedList =
            _tasksListScreenUiState.value.tasksList.sortedByDescending { task -> task.taskTitle }
                .reversed()
        updateTasksList(sortedList)
    }

    private fun sortTasksByAlphaZA() {
        sortedList =
            _tasksListScreenUiState.value.tasksList.sortedByDescending { task -> task.taskTitle }
        updateTasksList(sortedList)
    }


    fun showSortByDialog() {
        updateShowSortByDialogState(show = true)

    }

    fun hideSortByDialog() {
        updateShowSortByDialogState(show = false)
    }


    fun search(searchingString: String) {
        updateSearchingString(searchingString)
        val selectedCategory = _tasksListScreenUiState.value.selectedCategory
        val filteredList = currentTaskList.filter { task ->
            task.taskTitle.contains(
                searchingString.trim()
            )
        }

        updateTasksList(filteredList)
    }


    fun activateSearchMode() {

        updateSearModeState(isActive = true)

    }

    fun deactivateSearchMode() {
        updateSearModeState(isActive = false)
        search("")
        val selectedCategory = _tasksListScreenUiState.value.selectedCategory
        filterByTaskCategory(selectedCategory)
    }


    fun showCreateTaskDialog() {
        updateShowCreateTaskDialogState(show = true)
    }

    fun hideCreateTaskDialog() {
        updateShowCreateTaskDialogState(show = false)
    }


    fun filterByTaskCategory(category: LocalCategoryEntity?) {
        updateSelectedCategory(category)

        if (category?.categoryId == null) {
            updateTasksList(currentTaskList)
        } else {
            val filteredTaskList =
                currentTaskList.filter { task -> task.categoryIdFk == category.categoryId }
            updateTasksList(filteredTaskList)
        }

        selectSortByOption(option = _tasksListScreenUiState.value.sortByDialogUiState.selectedOption)
    }


    private fun updateSortBySelectedOption(option: Int) {
        val newState = _tasksListScreenUiState.value.sortByDialogUiState
        _tasksListScreenUiState.update { currentState ->
            currentState.copy(
                sortByDialogUiState = currentState.sortByDialogUiState.copy(
                    selectedOption = option
                )
            )
        }
    }


    private fun updateShowSortByDialogState(show: Boolean) {
        _tasksListScreenUiState.update { currentState ->
            currentState.copy(showSortByDialog = show)
        }
    }

    private fun updateSearchingString(value: String) {
        _tasksListScreenUiState.update { currentState ->
            currentState.copy(searchingString = value)
        }
    }


    private fun updateSearModeState(isActive: Boolean) {
        _tasksListScreenUiState.update { currentState ->
            currentState.copy(isSearchModeActive = isActive)
        }
    }


    private fun updateShowCreateTaskDialogState(show: Boolean) {
        _tasksListScreenUiState.update { currentState ->
            currentState.copy(showCreateTaskDialog = show)
        }
    }


    private fun updateSelectedCategory(category: LocalCategoryEntity?) {
        _tasksListScreenUiState.update { currentState ->
            currentState.copy(selectedCategory = category)
        }
    }

    private fun updateCategoriesList(categoryList: List<LocalCategoryEntity>) {
        _tasksListScreenUiState.update { currentState ->
            currentState.copy(categoryList = categoryList)
        }
    }

    private fun updateTasksList(tasksList: List<LocalTaskEntity>) {
        _tasksListScreenUiState.update { currentState ->
            currentState.copy(tasksList = tasksList)
        }
    }


}