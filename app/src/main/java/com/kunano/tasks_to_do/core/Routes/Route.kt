import androidx.annotation.StringRes
import com.kunano.tasks_to_do.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val route: String, @StringRes val resourceId: Int) {

    data object DeepLinksDirection {
        val taskDetails: String =
            "https://www.example.com/{route}/{resourceId}/{taskKey}"
    }

    /** inside taskList nested graph **/
    @Serializable
    data object TasksListScreen : Route("TasksListScreen", R.string.tasks_list)

    /** inside taskList nested graph **/
    @Serializable
    data class TaskDetails(val taskKey: Long) : Route("TaskDetails", R.string.task_details)

    @Serializable
    data object ManageCategories : Route("ManageCategories", R.string.manage_categories)


    /** inside stats nested graph **/
    @Serializable
    data object StatsScreen : Route("StatsScreen", R.string.stats)


    @Serializable
    data class NoteScreen(val taskKey: Long) : Route("NoteScreen", R.string.note)


}