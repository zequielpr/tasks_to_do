

import Route
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.kunano.tasks_to_do.R
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomNavBarRoutes(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val label: Int,
    val startDestination: Route
) {

    /** Route of taskList nested graph **/
    @Serializable
    data object TasksList : BottomNavBarRoutes(
        "TasksList",
        R.drawable.list_icon,
        R.string.tasks_list,
        startDestination = Route.TasksListScreen
    )

    /** Route of stats nested graph **/
    @Serializable
    data object Stats : BottomNavBarRoutes(
        "Stats",
        R.drawable.stats_query,
        R.string.stats,
        startDestination = Route.StatsScreen
    )

}