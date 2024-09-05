package com.kunano.tasks_to_do

import BottomNavBarRoutes
import Route
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.kunano.tasks_to_do.core.notification_manager.CustomNotificationManager
import com.kunano.tasks_to_do.tasks_list.presentation.TasksListScreen
import com.kunano.tasks_to_do.core.theme.Tasks_to_doTheme
import com.kunano.tasks_to_do.stats.presentation.StatsScreen
import com.kunano.tasks_to_do.tasks_list.manage_category.ManageCategoriesScreen
import com.kunano.tasks_to_do.tasks_list.task_details.TaskDetailScreen
import com.kunano.tasks_to_do.tasks_list.task_details.notes.NoteScreen
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import android.net.Uri
import androidx.compose.ui.res.painterResource

val graphsRoutesList = listOf(BottomNavBarRoutes.TasksList, BottomNavBarRoutes.Stats)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        WindowCompat.setDecorFitsSystemWindows(window, false)
        CustomNotificationManager.createNotificationChannel(this)

        setContent {
            Tasks_to_doTheme {
                val bottomAppBarScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
                val navController = rememberNavController()
                val snackBarState by remember {
                    mutableStateOf(SnackbarHostState())

                }
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackBarState,
                            modifier = Modifier.imePadding()
                        )
                    },
                    bottomBar = {
                        bottomBar(
                            navController = navController,
                            bottomAppBarScrollBehavior
                        )
                    },
                    content = { padding ->
                        navHost(
                            navController = navController,
                            bottomAppBarScrollBehavior = bottomAppBarScrollBehavior,
                            innerPadding = padding,
                            snackBarHostState = snackBarState,
                            modifier = Modifier.nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection),
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
    bottomAppBarScrollBehavior: BottomAppBarScrollBehavior,
    snackBarHostState: SnackbarHostState,
    innerPadding: PaddingValues,
    modifier: Modifier,
) {

    val enterTransition = slideInVertically(
        initialOffsetY = { 1000 }, // Start the slide from the right
        animationSpec = tween(durationMillis = 300) // Customize the animation speed
    ) + fadeIn(animationSpec = tween(durationMillis = 200))
    NavHost(
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popExitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        modifier = modifier,
        navController = navController,
        startDestination = BottomNavBarRoutes.TasksList,

        ) {
        navigation<BottomNavBarRoutes.TasksList>(startDestination = Route.TasksListScreen) {
            composable<Route.TasksListScreen> {
                TasksListScreen(
                    paddingValues = innerPadding,
                    navigate = { navigate(navController, it) }
                )
            }
            composable<Route.TaskDetails>(
                deepLinks = listOf(navDeepLink {
                    action = Intent.ACTION_VIEW
                    uriPattern = Route.DeepLinksDirection.taskDetails
                    argument("route") {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                    argument("resourceId") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                    argument("taskKey") {
                        type = NavType.LongType
                        defaultValue = -1
                    }
                }),
            ) {
                TaskDetailScreen(
                    snackbarHostState = snackBarHostState,
                    contentPadding = innerPadding,
                    bottomAppBarScrollBehavior = bottomAppBarScrollBehavior,
                    navigate = { route -> navigate(navController, route) },
                    navigateBack = { navigateBack(navController) })
            }

            composable<Route.ManageCategories> {
                ManageCategoriesScreen(
                    paddingValues = innerPadding,
                    navigate = {},
                    navigateBack = { navigateBack(navController) }
                )
            }

            composable<Route.NoteScreen> {
                NoteScreen(
                    navigateBack = { navigateBack(navController) },
                    paddingValues = innerPadding
                )
            }
        }

        navigation<BottomNavBarRoutes.Stats>(startDestination = Route.StatsScreen) {
            composable<Route.StatsScreen> {
                StatsScreen(paddingValues = innerPadding, navigate = { /*TODO*/ })
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
                    Icon(painter = painterResource(id = screen.icon), contentDescription = screen.route)
                },
                label = { Text(stringResource(screen.label)) },
                selected = currentDestination?.hierarchy?.any {
                    println("route ${it.route}")
                    it.route.toString().split(".").last() == screen.route
                } == true,
                onClick = {
                    println("to navigate screen: $screen")

                    navController.navigate(screen) {
                        // Pop up to the start destination to prevent stacking
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop =
                            true // Launch the destination without duplicating it in the back stack
                        restoreState = true
                    }
                }
            )
        }

    }
}


fun navigate(navController: NavController, route: Route) {
    navController.navigate(route) {
        // Pop up to the start destination to prevent stacking
        popUpTo(route) {
            saveState = true
        }
        launchSingleTop =
            true // Launch the destination without duplicating it in the back stack
        restoreState = true
    }
}

fun navigateBack(navController: NavController) {
    navController.popBackStack()
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