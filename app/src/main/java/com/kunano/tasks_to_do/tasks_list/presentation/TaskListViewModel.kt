package com.kunano.tasks_to_do.tasks_list.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskListViewModel : ViewModel() {



    var testData : MutableLiveData<List<String>> = MutableLiveData(listOf("Task 1", "Task 2"))



    init {
        val tasksList = arrayListOf<String>()

        for(i in 100..200){
            tasksList.add("Task $i")
        }

        testData.postValue(tasksList)

    }


}