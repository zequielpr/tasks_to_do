package com.kunano.tasks_to_do.tasks_list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor() : ViewModel() {

    //Intern updates
    private val _tasksListScreenUiState = MutableStateFlow(TasksListScreenUiState())

    //Read only
    val tasksListScreedUiState = _tasksListScreenUiState


    //Internal updates
    private var _testData: MutableLiveData<List<String>> = MutableLiveData(listOf())

    //Read only
    val tasksList: LiveData<List<String>> get() = _testData


    var categoriesList: MutableLiveData<List<String>> = MutableLiveData(
        listOf(
            "category 1",
            "Task 2",
            "category 3",
            "Task 4",
            "category 5",
            "Task 6",
        )
    )

    init {
        val tasksList = arrayListOf<String>()
        val catList = arrayListOf<String>()
        for (i in 100..200) {
            tasksList.add("Task $i")
        }
        updateTasksList(tasksList)


        for (i in 3..7) {
            catList.add("category$i")
        }

        updateCategoriesList(categoryList = catList)

    }

    fun selectSortByOption(option: Int){
        updateSortBySelectedOption(option = option)
        hideSortByDialog()
    }

    fun showSortByDialog(){
        updateShowSortByDialogState(show = true)
    }
    fun hideSortByDialog(){
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


    private fun updateShowSortByDialogState(show: Boolean){
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

    private fun updateTasksList(tasksList: List<String>) {
        _tasksListScreenUiState.update { currentState ->
            currentState.copy(tasksList = tasksList)
        }
    }


}