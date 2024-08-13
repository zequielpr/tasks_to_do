package com.kunano.tasks_to_do.tasks_list.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskListViewModel : ViewModel() {


    var testData: MutableLiveData<List<String>> = MutableLiveData(listOf("Task 1", "Task 2"))

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

        testData.postValue(tasksList)

        for(i in 5..10){
            catList.add("category$i")
        }
        categoriesList.postValue(catList)

    }


    fun selectTaskCategory(category: String){

        val tasksList = arrayListOf<String>()


        for (i in 100..200) {
            tasksList.add("$category: $i")
        }

        testData.postValue(tasksList)
    }


}