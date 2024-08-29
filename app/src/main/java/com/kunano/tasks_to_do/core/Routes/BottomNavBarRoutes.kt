

import Route
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.graphics.vector.ImageVector
import com.kunano.tasks_to_do.R
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomNavBarRoutes(
    val route: String,
    @Contextual val icon: ImageVector,
    @StringRes val label: Int,
    val startDestination: Route
) {

    /** Route of taskList nested graph **/
    @Serializable
    data object TasksList : BottomNavBarRoutes(
        "TasksList",
        Icons.AutoMirrored.Filled.List,
        R.string.tasks_list,
        startDestination = Route.TasksListScreen
    )

    /** Route of stats nested graph **/
    @Serializable
    data object Stats : BottomNavBarRoutes(
        "Stats",
        Icons.Default.AccountCircle,
        R.string.stats,
        startDestination = Route.StatsScreen
    )

}