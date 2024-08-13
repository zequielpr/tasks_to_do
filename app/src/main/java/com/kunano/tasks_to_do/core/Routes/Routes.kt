package com.kunano.tasks_to_do.core.Routes

import androidx.annotation.StringRes
import com.kunano.tasks_to_do.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val route: String, @StringRes val resourceId: Int) {




    /** inside taskList nested graph **/
    @Serializable
    data object TasksListScreen : Routes("TasksListScreen", R.string.tasks_list)

    /** inside taskList nested graph **/
    @Serializable
    data class TaskDetails(val taskKey: String) : Routes("TaskDetails", R.string.task_details)


    /** inside stats nested graph **/
    @Serializable
    data object StatsScreen : Routes("StatsScreen", R.string.stats)


}