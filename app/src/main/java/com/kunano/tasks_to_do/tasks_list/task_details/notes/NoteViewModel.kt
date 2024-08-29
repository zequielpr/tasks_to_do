package com.kunano.tasks_to_do.tasks_list.task_details.notes

import Route
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle): ViewModel() {
    private val _noteScreenState: MutableStateFlow<NoteScreenState> = MutableStateFlow(
        NoteScreenState()
    )

    val noteScreenState: StateFlow<NoteScreenState> = _noteScreenState


    init {
        val arg = savedStateHandle.toRoute<Route.NoteScreen>()
        val taskKey = arg.taskKey
        viewModelScope.launch {
            _noteScreenState.update { currentState ->
                currentState.copy(noteTitle = "note title: $taskKey", note = "note")
            }
        }

    }
}