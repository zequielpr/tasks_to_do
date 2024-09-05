package com.kunano.tasks_to_do.tasks_list.task_details.notes

import Route
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kunano.tasks_to_do.core.data.TaskRepository
import com.kunano.tasks_to_do.core.data.model.entities.LocalTaskEntity
import com.kunano.tasks_to_do.core.data.model.entities.Note
import com.kunano.tasks_to_do.core.utils.Utils
import com.kunano.tasks_to_do.tasks_list.task_details.AttachedNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.FormatStyle
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _noteScreenState: MutableStateFlow<NoteScreenState> = MutableStateFlow(
        NoteScreenState()
    )

    private val taskKey = savedStateHandle.toRoute<Route.NoteScreen>().taskKey
    private var currentTask: LocalTaskEntity? = null;
    val noteScreenState: StateFlow<NoteScreenState> = _noteScreenState

    init {
        fetchTask()
        fetchTaskLive()
    }

    private fun fetchTaskLive(){
        viewModelScope.launch {
            taskRepository.getTaskLive(taskKey).collect{
                currentTask = it
            }
        }
    }

    private fun fetchTask(){
        viewModelScope.launch {
            currentTask = taskRepository.getTask(taskKey)
            currentTask?.let {
                populateNoteScreen(it.note)
            }
        }
    }



    fun onTitleChange(value: String){
        setTitleInSourceOfTruth(value)
        updateTitleUiState(value)
    }

    fun onContentChange(value: String){
        setNoteInSourceOfTruth(value)
        updateNoteContentState(value)
    }



    private fun setTitleInSourceOfTruth(title: String){
        val currentDate = LocalDateTime.now()
        viewModelScope.launch {
            currentTask?.let {
                taskRepository.updateTask(it.copy(note = it.note.copy(title = title, lastModifiedDate = currentDate)))
            }
        }
    }

    private fun setNoteInSourceOfTruth(note: String){
        val currentDate = LocalDateTime.now()
        viewModelScope.launch {
            currentTask?.let {
                taskRepository.updateTask(it.copy(note = it.note.copy(content = note, lastModifiedDate = currentDate)))
            }
        }
    }




    private fun updateTitleUiState(value: String){
        _noteScreenState.update { currentState ->
            currentState.copy(noteTitle = value)
        }
    }

    private fun updateNoteContentState(value: String){
        _noteScreenState.update { currentState ->
            currentState.copy(note = value)
        }
    }



    private fun populateNoteScreen(note: Note?) {
        note?.let {
            var lastModifiedDate: String? = null
            it.lastModifiedDate?.let { date ->
                lastModifiedDate = Utils.Companion.localDateToString(date, FormatStyle.SHORT)
            }
            _noteScreenState.update { currentState ->

                currentState.copy(noteTitle = it.title, note = it.content, lastModified = lastModifiedDate)
            }
        }
    }



}