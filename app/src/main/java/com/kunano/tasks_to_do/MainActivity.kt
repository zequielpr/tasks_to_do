package com.kunano.tasks_to_do

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.kunano.tasks_to_do.core.Routes.BottomNavBarRoutes
import com.kunano.tasks_to_do.core.Routes.Routes
import com.kunano.tasks_to_do.tasks_list.presentation.TaskListViewModel
import com.kunano.tasks_to_do.tasks_list.presentation.TasksListScreen
import com.kunano.tasks_to_do.core.theme.Tasks_to_doTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

val graphsRoutesList = listOf(BottomNavBarRoutes.TasksList, BottomNavBarRoutes.Stats)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            Tasks_to_doTheme {
                val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { bottomBar(navController = navController, scrollBehavior) },
                    content = { padding ->
                        navHost(
                            navController = navController,
                            innerPadding = padding,
                            bottomAppBarScrollBehavior = scrollBehavior
                        )
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun navHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    bottomAppBarScrollBehavior: BottomAppBarScrollBehavior
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavBarRoutes.TasksList
    ) {

        navigation<BottomNavBarRoutes.TasksList>(startDestination = Routes.TasksListScreen) {
            composable<Routes.TasksListScreen> {
                TasksListScreen(innerPadding, bottomAppBarScrollBehavior)
            }
            composable<Routes.TaskDetails> {
            }
        }

        navigation<BottomNavBarRoutes.Stats>(startDestination = Routes.StatsScreen) {
            composable<Routes.StatsScreen> {

            }
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun bottomBar(navController: NavController, scrollBehavior: BottomAppBarScrollBehavior) {

    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        scrollBehavior = scrollBehavior
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination


        graphsRoutesList.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(screen.label)) },
                selected = currentDestination?.hierarchy?.any {
                    println("route ${it.route}")
                    it.route.toString().split(".").last() == screen.route
                } == true,
                onClick = {
                    navController.navigate(screen) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(screen) {
                            saveState = true
                        }

                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = false
                    }
                }
            )
        }

    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun tasksListPreview() {
    //TasksListScreen(PaddingValues(), bottomAppBarScrollBehavior = BottomAppBarScrollBehavior)
}

@HiltAndroidApp
class MyApplication : Application() {
    // You can initialize other things here if needed
}