package com.kunano.tasks_to_do.stats.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(): ViewModel() {
    private val _statsScreenState: MutableStateFlow<StatsScreenState> = MutableStateFlow(StatsScreenState())

    val statsScreenState: StateFlow<StatsScreenState> = _statsScreenState



    init {
        updatePendingTasks(0)
        updateCompletedTasks(0)
    }


    private fun updatePendingTasks(pendingTasks: Int){
        _statsScreenState.update { currentState ->
            currentState.copy(pendingTask = pendingTasks.toString())
        }
    }

    private fun updateCompletedTasks(completedTasks: Int){
        _statsScreenState.update { currentState ->
            currentState.copy(pendingTask = completedTasks.toString())
        }
    }


}