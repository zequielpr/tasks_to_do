package com.kunano.tasks_to_do.tasks_list.task_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor() :
    ViewModel() {
    private val _taskDetailUiState: MutableStateFlow<TaskDetailsUiState> =
        MutableStateFlow(TaskDetailsUiState())

    val taskDetail: StateFlow<TaskDetailsUiState> = _taskDetailUiState



    fun showTimePicker(){
        updateShowTimePicker(show = true)
    }

    fun hideTimePicker(){
        updateShowTimePicker(show = false)
    }


    fun showDatePicker(){
        updateShowDueDatePicker(show = true)
    }

    fun hideDatePicker(){
        updateShowDueDatePicker(show = false)
    }

    fun selectDropDownMenuAction(action: Int){

    }


    fun updateSubTaskState(subTaskId: String, state: Boolean) {
        updateSubTaskInput(isDone = state, subTaskId = subTaskId)
    }

    fun addNewSubTaskField() {
        addNewSubTaskInput()
    }

    fun deleteSubTaskField(subTaskInputState: SubTaskInputState) {
        deleteSubTaskInput(subTaskInputState)
    }

    fun onSubTaskInputChanges(subTaskInputState: SubTaskInputState, value: String) {
        updateSubTaskInput(input = value, subTaskId = subTaskInputState.subTaskId)
    }

    fun setTaskCategory(category: String?) {
        println("category: $category")
    }

    fun setDueDate(dueDate: Long?) {

    }

    fun setTimeReminder(time: Long) {

    }


    private fun updateShowTimePicker(show: Boolean){
        _taskDetailUiState.update { currentState ->
            currentState.copy(showTimePicker = show)
        }
    }

    private fun updateShowDueDatePicker(show: Boolean){
        _taskDetailUiState.update { currentState ->
            currentState.copy(showDueDatePicker = show)
        }
    }


    private fun updateSubTaskInput(input: String, subTaskId: String) {

        _taskDetailUiState.update { currentState ->

            val updatedSubTaskInputStates: List<SubTaskInputState> =
                currentState.subTasksInputInputState.map { subTask ->
                    if (subTask.subTaskId == subTaskId) {
                        subTask.copy(subTaskName = input)
                    } else {
                        subTask
                    }
                }

            currentState.copy(
                subTasksInputInputState = updatedSubTaskInputStates
            )

        }
    }


    private fun updateSubTaskInput(isDone: Boolean, subTaskId: String) {

        _taskDetailUiState.update { currentState ->

            val updatedSubTaskInputStates: List<SubTaskInputState> =
                currentState.subTasksInputInputState.map { subTask ->
                    if (subTask.subTaskId == subTaskId) {
                        subTask.copy(isSubTaskDone = isDone)
                    } else {
                        subTask
                    }
                }

            currentState.copy(
                subTasksInputInputState = updatedSubTaskInputStates
            )

        }
    }


    private fun deleteSubTaskInput(subTaskInputState: SubTaskInputState) {
        viewModelScope.launch {


            _taskDetailUiState.update { currentState ->
                val updateList: List<SubTaskInputState> =
                    currentState.subTasksInputInputState.filter { it.subTaskId != subTaskInputState.subTaskId }
                currentState.copy(subTasksInputInputState = updateList)
            }
        }
    }

    private fun addNewSubTaskInput() {
        viewModelScope.launch {
            val subTaskInputStateInput =
                SubTaskInputState(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    null,
                    false
                )

            _taskDetailUiState.update { currentState ->
                currentState.copy(subTasksInputInputState = (currentState.subTasksInputInputState + subTaskInputStateInput) as MutableList<SubTaskInputState>)
            }
        }
    }


    private fun updateTaskCategory(category: String) {
        //Send set in the source of truth
    }


}