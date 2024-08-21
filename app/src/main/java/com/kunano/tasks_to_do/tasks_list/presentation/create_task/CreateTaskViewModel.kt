package com.kunano.tasks_to_do.tasks_list.presentation.create_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunano.tasks_to_do.core.utils.Utils
import com.kunano.tasks_to_do.tasks_list.presentation.states.CreateCategoryUiState
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
class CreateTaskViewModel @Inject constructor(): ViewModel() {
    //The business logic can be updated from this view model
    private val _createTaskUiState = MutableStateFlow(CreateTaskUiState())
    private val _createCategoryUiState = MutableStateFlow(CreateCategoryUiState())

    private val _showIntroduceTaskNameToast = MutableSharedFlow<Boolean>()

    //Read only
    val createTaskUiState: StateFlow<CreateTaskUiState> = _createTaskUiState.asStateFlow()
    val createCategoryUiState: StateFlow<CreateCategoryUiState> = _createCategoryUiState.asStateFlow()

    val showIntroduceTaskNameToast: SharedFlow<Boolean> = _showIntroduceTaskNameToast.asSharedFlow()



    private var categoriesList =  listOf(
        "category 1",
        "Task 2",
        "category 3",
    )

    init {
        _createTaskUiState.update { createTaskUiState ->
            createTaskUiState.copy(categoryList = categoriesList)
        }
    }


    fun createTask() {
        //Create task
        val taskName: String = createTaskUiState.value.taskName
        val dueDate: Long = createTaskUiState.value.selectedDateInMilliseconds
        val category: String? = createTaskUiState.value.selectedCategoryInBottomSheet

        //Clear task name
        if (taskName.isEmpty()){
            showIntroduceTaskNameToast(true)
            return
        }
        onChangeName("")
        viewModelScope.launch {

        }
    }



    fun createCategory(){
        val categoryName = _createCategoryUiState.value.categoryName
        if (categoryName.isEmpty()){
            showCategoryErrorMessage()
            return
        }
        hideCategoryErrorMessage()

        //Create category
        updateSelectedCategory(categoryName)
        onCategoryNameChange("")
        hideCreateCategoryDialog()


    }


    fun showIntroduceTaskNameToast(show: Boolean){
        viewModelScope.launch {
            _showIntroduceTaskNameToast.emit(show)
        }
    }


    fun  showCreateCategoryDialog(){
        updateShowCategoryDialogState(show = true)
    }
    fun  hideCreateCategoryDialog(){
        updateShowCategoryDialogState( show = false)
    }


    private fun showCategoryErrorMessage(){
        updateShowCategoryErrorMessageState(show = true)
    }

    private fun hideCategoryErrorMessage(){
        updateShowCategoryErrorMessageState(show = false)
    }

    fun onCategoryNameChange(categoryName: String){
        updateCategoryTextFieldContent(categoryName)
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

            val dayOfMonth = Utils.millToLocalDateTie(date).dayOfMonth
            updateDayOfMonth(day = dayOfMonth)
        }
    }


    fun selectTaskCategory(category: String?) {
        updateSelectedCategory(category = category)
    }

    fun onChangeName(newValue: String) {
        updateTaskName(name = newValue)
    }


    private fun updateShowCategoryDialogState(show: Boolean){
        _createCategoryUiState.update { currentState ->
            currentState.copy(showCreateCategoryDialog = show)
        }
    }


    private fun updateCategoryTextFieldContent(value: String){
        _createCategoryUiState.update { currentState ->
            currentState.copy(categoryName = value)
        }
    }

    private fun updateShowCategoryErrorMessageState(show: Boolean){
        _createCategoryUiState.update { currentState ->
            currentState.copy(showErrorMessage = show)
        }
    }

    private fun updateShowCreateCategoryDialogState(state: Boolean){
        _createCategoryUiState.update { currentState->
            currentState.copy(showErrorMessage = state)
        }
    }



    private fun updateCategoriesDropDownMenuState(show: Boolean){
        _createTaskUiState.update { currentState ->
            currentState.copy(showCategoriesDropDownMenu = show)
        }
    }

    private fun updateShowDatePickerState(show: Boolean){
        _createTaskUiState.update { createTaskUiState ->
            createTaskUiState.copy(showDatePicker = show)
        }
    }


    private fun updateSelectedDateInMilliseconds(date: Long){
        _createTaskUiState.update { createTaskUiState ->
            createTaskUiState.copy(selectedDateInMilliseconds = date)
        }
    }

    private fun updateDayOfMonth(day: Int){
        _createTaskUiState.update { createTaskUiState ->
            createTaskUiState.copy(selectedDayOfMonth = day)
        }
    }

    private fun updateSelectedCategory(category: String?){
        _createTaskUiState.update {createTaskUiState ->
            createTaskUiState.copy(selectedCategoryInBottomSheet = category)
        }
    }

    private fun updateTaskName(name: String){
        _createTaskUiState.update {createTaskUiState ->
            createTaskUiState.copy(taskName = name)
        }
    }


}