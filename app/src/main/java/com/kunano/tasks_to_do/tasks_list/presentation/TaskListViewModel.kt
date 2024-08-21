package com.kunano.tasks_to_do.tasks_list.presentation

import android.database.Observable
import androidx.collection.objectListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor() : ViewModel() {

    //Internal updates
    private var _testData: MutableLiveData<List<String>> = MutableLiveData(listOf())

    //Read only
    val tasksList: LiveData<List<String>> get() = _testData


    var categoriesList: MutableLiveData<List<String>> =
        MutableLiveData(
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
        _testData.postValue(tasksList)

        for (i in 5..10) {
            catList.add("category$i")
        }
        categoriesList.postValue(catList)

    }






    fun filterByTaskCategory(category: String) {

    }




}