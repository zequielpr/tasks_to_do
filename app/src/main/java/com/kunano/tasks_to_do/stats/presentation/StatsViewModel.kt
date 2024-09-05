package com.kunano.tasks_to_do.stats.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunano.tasks_to_do.core.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(private val taskRepository: TaskRepository): ViewModel() {
    private val _statsScreenState: MutableStateFlow<StatsScreenState> = MutableStateFlow(StatsScreenState())

    val statsScreenState: StateFlow<StatsScreenState> = _statsScreenState



    init {

        viewModelScope.launch {
            taskRepository.getPendingTask().collect{
               updatePendingTasks(it)
            }
        }

        viewModelScope.launch {
            taskRepository.getCompletedTask().collect{
                updateCompletedTasks(it)
            }
        }

    }


    private fun updatePendingTasks(pendingTasks: Int){
        _statsScreenState.update { currentState ->
            currentState.copy(pendingTask = pendingTasks.toString())
        }
    }

    private fun updateCompletedTasks(completedTasks: Int){
        _statsScreenState.update { currentState ->
            currentState.copy(completedTask = completedTasks.toString())
        }
    }


}