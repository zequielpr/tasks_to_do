package com.kunano.tasks_to_do.tasks_list.presentation

import Route
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.toRoute
import com.kunano.tasks_to_do.core.data.SubTaskRepository
import com.kunano.tasks_to_do.core.data.TaskRepository
import com.kunano.tasks_to_do.core.data.model.entities.LocalTaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
) :
    ViewModel() {

    //Intern updates
    private val _tasksListScreenUiState = MutableStateFlow(TasksListScreenUiState())

    //Read only
    val tasksListScreedUiState = _tasksListScreenUiState





    init {
        viewModelScope.launch {
            taskRepository.getTasksList().collect {
                updateTasksList(it)
            }
        }
        val tasksList = arrayListOf<String>()
        val catList = arrayListOf<String>()
        for (i in 100..200) {
            tasksList.add("Task $i")
        }

        for (i in 3..7) {
            catList.add("category$i")
        }

        updateCategoriesList(categoryList = catList)

    }


    fun selectSortByOption(option: Int) {
        updateSortBySelectedOption(option = option)
        hideSortByDialog()
    }

    fun showSortByDialog() {
        updateShowSortByDialogState(show = true)

    }

    fun hideSortByDialog() {
        updateShowSortByDialogState(show = false)
    }


    fun search(searchingString: String) {
        updateSearchingString(searchingString)
    }


    fun activateSearchMode() {
        updateSearModeState(isActive = true)
    }

    fun deactivateSearchMode() {
        updateSearModeState(isActive = false)
    }


    fun showCreateTaskDialog() {
        updateShowCreateTaskDialogState(show = true)
    }

    fun hideCreateTaskDialog() {
        updateShowCreateTaskDialogState(show = false)
    }


    fun filterByTaskCategory(category: String?) {
        updateSelectedCategory(category)
    }


    private fun updateSortBySelectedOption(option: Int) {
        val newState = _tasksListScreenUiState.value.sortByDialogData
        _tasksListScreenUiState.update { currentState ->
            currentState.copy(sortByDialogData = currentState.sortByDialogData.copy(selectedOption = option))
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


    private fun updateSelectedCategory(category: String?) {
        _tasksListScreenUiState.update { currentState ->
            currentState.copy(selectedCategory = category)
        }
    }

    private fun updateCategoriesList(categoryList: List<String>) {
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