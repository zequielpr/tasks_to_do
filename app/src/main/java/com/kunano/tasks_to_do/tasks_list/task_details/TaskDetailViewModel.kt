package com.kunano.tasks_to_do.tasks_list.task_details

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kunano.tasks_to_do.R
import com.kunano.tasks_to_do.core.StringsResourceRepository
import com.kunano.tasks_to_do.core.data.CategoryRepository
import com.kunano.tasks_to_do.core.data.SubTaskRepository
import com.kunano.tasks_to_do.core.data.TaskRepository
import com.kunano.tasks_to_do.core.data.model.entities.LocalCategoryEntity
import com.kunano.tasks_to_do.core.data.model.entities.LocalSubTaskEntity
import com.kunano.tasks_to_do.core.data.model.entities.LocalTaskEntity
import com.kunano.tasks_to_do.core.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.FormatStyle
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository,
    private val subTaskRepository: SubTaskRepository,
    private val stringsResourceRepository: StringsResourceRepository
) :
    ViewModel() {

    val taskKey: Long = savedStateHandle.toRoute<Route.TaskDetails>().taskKey
    private val _taskDetailUiState: MutableStateFlow<TaskDetailsUiState> =
        MutableStateFlow(TaskDetailsUiState())

    val taskDetail: StateFlow<TaskDetailsUiState> = _taskDetailUiState

    private var currentTask: LocalTaskEntity? = null


    init {
        viewModelScope.launch {
            fetchTask()
            fetCategory()
            fetchSubTasksList()
        }
    }


    private suspend fun fetchTask() {
        currentTask = taskRepository.getTask(taskKey)
        currentTask?.let {
            updateTaskTitleUiState(it.taskTitle)
            populateDueDate(it.dueDate)
        }
        println("task category ${currentTask?.categoryIdFk}")
    }


    fun selectDropDownMenuAction(action: Int) {

        viewModelScope.launch {
            when (action) {
                R.string.delete -> {
                    deleteTask()
                }

                R.string.duplicate_task -> {
                    duplicateTask()
                }

                R.string.mark_as_done -> {
                    markTaskAsDone()
                }

                else -> {} //Default action
            }
        }
    }


    private suspend fun showSnackBar(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration
    ): SnackbarResult {
        return _taskDetailUiState.value.snackBarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = true,
            // Defaults to SnackbarDuration.Short
            duration = duration
        )
    }

    private suspend fun markTaskAsDone() {

        currentTask?.let {
            val isSuccessful = taskRepository.updateTask(it.copy(isCompleted = true))
            if (isSuccessful) {
                val result = showSnackBar(
                    message = stringsResourceRepository.getStringResource(R.string.task_marked_as_done),
                    duration = SnackbarDuration.Short,
                    actionLabel = stringsResourceRepository.getStringResource(R.string.undo),
                )
                when (result) {
                    SnackbarResult.Dismissed -> {}
                    SnackbarResult.ActionPerformed -> {
                        desMarkTaskAsDone()
                    } //Undo action
                }
            }
        }
    }

    private suspend fun duplicateTask() {
        currentTask?.let {
            val newTask = taskRepository.insertAndGetTask(
                it.copy(
                    taskId = null,
                    isCompleted = false
                )
            ) //A new id will be generate

            //Copy the subTask of the current task
            newTask?.let {
                _taskDetailUiState.value.subTasksInputInputState.forEach { subTask ->
                    addSubTask(
                        subTask.copy(
                            taskIdFk = newTask.taskId!!.toLong(),
                            subTaskId = UUID.randomUUID().toString()
                        )
                    )
                }

                val result = showSnackBar(
                    message = stringsResourceRepository.getStringResource(R.string.task_duplicated),
                    duration = SnackbarDuration.Short,
                )
                when (result) {
                    SnackbarResult.Dismissed -> {}
                    SnackbarResult.ActionPerformed -> {
                        desMarkTaskAsDone()
                    } //Undo action
                }
            }
        }
    }

    private suspend fun deleteTask() {
        currentTask?.let {
            val isSuccessful = taskRepository.deleteTask(it)
            if (isSuccessful) {
                val result = showSnackBar(
                    message = stringsResourceRepository.getStringResource(R.string.task_deleted),
                    duration = SnackbarDuration.Short,
                    actionLabel = stringsResourceRepository.getStringResource(R.string.undo),
                )
                when (result) {
                    SnackbarResult.Dismissed -> {}
                    SnackbarResult.ActionPerformed -> {
                        taskRepository.insertTask(it)
                    } //Undo action
                }
            }
        }

    }

    private suspend fun desMarkTaskAsDone() {
        currentTask?.let {
            taskRepository.updateTask(it.copy(isCompleted = false))
        }
    }


    private suspend fun fetchSubTasksList() {
        currentTask?.let {
            val subTasks = subTaskRepository.getSubTaskLIst(it.taskId!!.toLong())
            populateSubTaskList(subTasks)
        }
    }


    private suspend fun fetCategory() {
        currentTask?.categoryIdFk?.let {
            val currentCategory = categoryRepository.getCategoryById(it)
            updateTaskCategoryUiState(currentCategory.categoryName)
        }
    }


    fun updateTaskTitle(title: String) {

        currentTask?.let {
            updateTaskTitleUiState(title)
            it.taskTitle = title
            viewModelScope.launch {
                taskRepository.updateTask(it)
            }
        }
    }


    fun showTimePicker() {
        updateShowTimePicker(show = true)
    }

    fun hideTimePicker() {
        updateShowTimePicker(show = false)
    }


    fun showDatePicker() {
        updateShowDueDatePicker(show = true)
    }

    fun hideDatePicker() {
        updateShowDueDatePicker(show = false)
    }


    fun updateSubTaskState(subTask: LocalSubTaskEntity, state: Boolean) {
        updateSubTaskInput(isDone = state, subTaskId = subTask.subTaskId)


        updateSubTask(subTask.copy(isCompleted = state))
    }

    fun addNewSubTaskField() {
        addNewSubTaskInputUiState()
    }

    fun deleteSubTaskField(subTask: LocalSubTaskEntity) {
        deleteSubTaskInput(subTask)
        deleteSubTask(subTask)
    }

    fun onSubTaskInputChanges(subTask: LocalSubTaskEntity, value: String) {
        updateSubTaskInput(input = value, subTaskId = subTask.subTaskId)
        if (value.isEmpty()) {
            deleteSubTask(subTask)
        } else {

            addSubTask(subTask.copy(title = value))
        }
    }


    //Update subtask in the source of truth
    private fun addSubTask(subTaskEntity: LocalSubTaskEntity) {
        viewModelScope.launch {
            subTaskRepository.insertSubTask(subTaskEntity)
        }
    }

    private fun updateSubTask(subTaskEntity: LocalSubTaskEntity) {
        viewModelScope.launch {
            subTaskRepository.updateSubTask(subTaskEntity)
        }
    }

    private fun deleteSubTask(subTaskEntity: LocalSubTaskEntity) {
        viewModelScope.launch {
            subTaskRepository.deleteSubTask(subTaskEntity)
        }
    }

    fun updateTaskCategory(category: LocalCategoryEntity?) {
        viewModelScope.launch {
            currentTask?.let {
                it.categoryIdFk = category?.categoryId
                taskRepository.updateTask(it)
                updateTaskCategoryUiState(category?.categoryName)
            }

        }
    }

    fun setDueDate(dueDate: Long?) {
       viewModelScope.launch {
           currentTask?.let {
               val isSuccessful = taskRepository.updateTask(it.copy(dueDate = dueDate!!))
               if (isSuccessful){
                   populateDueDate(dueDate)
               }
               hideDatePicker()
           }
       }
    }

    fun setTimeReminder(time: Long) {

    }


    private fun updateShowTimePicker(show: Boolean) {
        _taskDetailUiState.update { currentState ->
            currentState.copy(showTimePicker = show)
        }
    }

    private fun updateShowDueDatePicker(show: Boolean) {
        _taskDetailUiState.update { currentState ->
            currentState.copy(showDueDatePicker = show)
        }
    }


    private fun populateDueDate(dueDate: Long) {
        val dueDateString: String =
            Utils.Companion.localDateToString(dueDate, FormatStyle.SHORT).split(",").first()
        _taskDetailUiState.update { currentState ->
            currentState.copy(dueDate = dueDateString, dueDateLong = dueDate)
        }
    }

    private fun populateSubTaskList(subTasksList: List<LocalSubTaskEntity>) {
        _taskDetailUiState.update { currentState ->
            currentState.copy(subTasksInputInputState = subTasksList)
        }
    }


    private fun updateSubTaskInput(input: String, subTaskId: String) {

        _taskDetailUiState.update { currentState ->

            val updatedSubTaskInputStates: List<LocalSubTaskEntity> =
                currentState.subTasksInputInputState.map { subTask ->
                    if (subTask.subTaskId == subTaskId) {
                        subTask.copy(title = input)
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


        val updatedSubTaskInputStates: List<LocalSubTaskEntity> =
            _taskDetailUiState.value.subTasksInputInputState.map { subTask ->
                if (subTask.subTaskId == subTaskId) {
                    println("update subTask: $subTaskId")
                    subTask.copy(isCompleted = isDone)
                } else {
                    subTask
                }
            }

        _taskDetailUiState.update { currentState ->
            println("update")
            currentState.copy(
                subTasksInputInputState = updatedSubTaskInputStates
            )

        }
    }


    private fun deleteSubTaskInput(subTask: LocalSubTaskEntity) {
        viewModelScope.launch {


            _taskDetailUiState.update { currentState ->
                val updateList: List<LocalSubTaskEntity> =
                    currentState.subTasksInputInputState.filter { it.subTaskId != subTask.subTaskId }
                currentState.copy(subTasksInputInputState = updateList)
            }
        }
    }

    private fun addNewSubTaskInputUiState() {
        viewModelScope.launch {
            val subTaskField = LocalSubTaskEntity(
                subTaskId = UUID.randomUUID().toString(),
                taskIdFk = currentTask?.taskId!!.toLong(),
                title = "",
                isCompleted = false
            )

            _taskDetailUiState.update { currentState ->
                currentState.copy(subTasksInputInputState = (currentState.subTasksInputInputState + subTaskField) as MutableList<LocalSubTaskEntity>)
            }
        }
    }

    private fun updateTaskTitleUiState(title: String) {
        _taskDetailUiState.update { currentState ->
            currentState.copy(taskTitle = title)
        }
    }


    private fun updateTaskCategoryUiState(category: String?) {
        _taskDetailUiState.update { currentState ->
            currentState.copy(category = category)
        }
    }


}