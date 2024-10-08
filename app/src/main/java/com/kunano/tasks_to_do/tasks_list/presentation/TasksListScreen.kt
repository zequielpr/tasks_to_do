package com.kunano.tasks_to_do.tasks_list.presentation

import Route
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.kunano.tasks_to_do.R
import com.kunano.tasks_to_do.core.data.model.entities.LocalCategoryEntity
import com.kunano.tasks_to_do.core.data.model.entities.LocalTaskEntity
import com.kunano.tasks_to_do.core.data.model.entities.Note
import com.kunano.tasks_to_do.core.utils.Utils
import com.kunano.tasks_to_do.core.utils.navigateBackButton
import com.kunano.tasks_to_do.core.utils.searchBar
import com.kunano.tasks_to_do.core.utils.sortByDialog
import com.kunano.tasks_to_do.tasks_list.create_task.createTaskBottomSheet
import java.time.LocalDateTime
import java.time.format.FormatStyle
import kotlin.reflect.KSuspendFunction1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksListScreen(
    paddingValues: PaddingValues,
    navigate: (route: Route) -> Unit,
    viewModel: TaskListViewModel = hiltViewModel()
) {

    val tasksListScreedUiState by viewModel.tasksListScreedUiState.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var floatingButtonExpanded by remember { mutableStateOf(true) }

    //If the top bar is visible the floating button must be expanded
    floatingButtonExpanded = scrollBehavior.state.heightOffset == 0.0f

    BackHandler(enabled = tasksListScreedUiState.isSearchModeActive) {
        viewModel.deactivateSearchMode()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            if (tasksListScreedUiState.isSearchModeActive) {
                TopAppBar(navigationIcon = {
                    navigateBackButton(
                        navigateBack = viewModel::deactivateSearchMode
                    )
                }, scrollBehavior = scrollBehavior, title = {
                    searchBar(
                        value = tasksListScreedUiState.searchingString, search = viewModel::search
                    )
                })
            } else {
                topBar(scrollBehavior = scrollBehavior,
                    tasksListScreenUiState = tasksListScreedUiState,
                    filter = viewModel::filterByTaskCategory,
                    activateSearchMode = viewModel::activateSearchMode,
                    showSortByDialog = viewModel::showSortByDialog,
                    manageCategories = { navigate(Route.ManageCategories) })
            }


            taskListContent(
                paddingValues,
                tasksListScreedUiState.tasksList,
                changeTaskState = viewModel::updateTaskState,
                navigateToTaskDetails = {
                    navigate(Route.TaskDetails(taskKey = it))
                }
            )
        }

        floatingActionButton(
            floatingButtonExpanded = floatingButtonExpanded,
            Modifier
                .align(AbsoluteAlignment.BottomRight)
                .padding(paddingValues)
                .padding(20.dp, 50.dp),
            showBottomSheet = viewModel::showCreateTaskDialog
        )

        if (tasksListScreedUiState.showCreateTaskDialog) {
            createTaskBottomSheet(onDismiss = viewModel::hideCreateTaskDialog)
        }

        if (tasksListScreedUiState.showSortByDialog) {

            sortByDialog(
                title = tasksListScreedUiState.sortByDialogUiState.title,
                selectedOption = tasksListScreedUiState.sortByDialogUiState.selectedOption,
                options = tasksListScreedUiState.sortByDialogUiState.options,
                selectOption = viewModel::selectSortByOption,
                onDismiss = viewModel::hideSortByDialog
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBar(
    scrollBehavior: TopAppBarScrollBehavior?,
    tasksListScreenUiState: TasksListScreenUiState,
    filter: (category: LocalCategoryEntity?) -> Unit,
    activateSearchMode: () -> Unit,
    showSortByDialog: () -> Unit,
    manageCategories: () -> Unit
) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            TaskCategoryCarousel(
                tasksListScreenUiState = tasksListScreenUiState, filter = filter
            )
        },
        actions = {

            IconButton(onClick = { dropDownMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert, contentDescription = null
                )
            }
            dropDownMenu(
                dropDownMenuExpanded,
                onDismissRequest = { dropDownMenuExpanded = false },
                activateSearchMode = activateSearchMode,
                showSortByDialog = showSortByDialog,
                manageCategories = manageCategories
            )
        },
        scrollBehavior = scrollBehavior,
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskCategoryCarousel(
    tasksListScreenUiState: TasksListScreenUiState, filter: (category: LocalCategoryEntity?) -> Unit
) {


    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            FilterChip(colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primary),
                shape = ShapeDefaults.Large,
                selected = tasksListScreenUiState.selectedCategory == null,
                onClick = { filter(null) },
                label = { Text(text = stringResource(id = R.string.all)) })
        }


        items(tasksListScreenUiState.categoryList) { category ->
            category?.let {
                categoryBtn(
                    category = it,
                    selectedCategory = tasksListScreenUiState.selectedCategory,
                    selectCategory = filter
                )
            }

        }


    }

}

@Composable
fun categoryBtn(
    category: LocalCategoryEntity,
    selectedCategory: LocalCategoryEntity?,
    selectCategory: (category: LocalCategoryEntity) -> Unit
) {
    FilterChip(colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primary),
        shape = ShapeDefaults.Large,
        selected = category.categoryId == selectedCategory?.categoryId,
        onClick = { selectCategory(category) },
        label = { Text(text = category.categoryName) })
}


@Composable
fun dropDownMenu(
    isExpanded: Boolean,
    onDismissRequest: () -> Unit,
    activateSearchMode: () -> Unit,
    showSortByDialog: () -> Unit,
    manageCategories: () -> Unit
) {

    DropdownMenu(expanded = isExpanded, onDismissRequest = { onDismissRequest() }) {
        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.search)) }, onClick = {
            activateSearchMode()
            onDismissRequest()
        })
        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.sort_by)) }, onClick = {
            showSortByDialog()
            onDismissRequest()
        })
        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.manage_categories)) },
            onClick = {
                manageCategories()
                onDismissRequest()
            })
    }

}


@Composable
fun floatingActionButton(
    floatingButtonExpanded: Boolean, modifier: Modifier, showBottomSheet: () -> Unit
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        expanded = floatingButtonExpanded,
        onClick = showBottomSheet,
        icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
        text = { Text(text = stringResource(id = R.string.create_task)) },
    )
}


@Composable
fun taskListContent(
    innerPadding: PaddingValues,
    tasksList: List<LocalTaskEntity>,
    changeTaskState: (task: LocalTaskEntity, state: Boolean) -> Unit,
    navigateToTaskDetails: (taskId: Long) -> Unit,
) {


    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp, 0.dp),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        items(tasksList) { task ->
            taskCard(
                task = task,
                navigateToTaskDetails = navigateToTaskDetails,
                changeTaskState = changeTaskState,
            )
        }
        item {
            Box(modifier = Modifier.height(20.dp))
        }
    }

}


@Composable
fun taskCard(
    task: LocalTaskEntity,
    changeTaskState: (task: LocalTaskEntity, state: Boolean) -> Unit,
    navigateToTaskDetails: (taskId: Long) -> Unit,
) {
    val verticalAlignment = Alignment.CenterVertically
    val iconsModifier: Modifier = Modifier.size(20.dp)


    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { navigateToTaskDetails(task.taskId!!) }) {
        Row(verticalAlignment = verticalAlignment) {
            Checkbox(checked = task.isCompleted,
                onCheckedChange = { state -> changeTaskState(task, state) })
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp),
                    text = task.taskTitle
                )
                Row(
                    verticalAlignment = verticalAlignment,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    //due date
                    Text(
                        text = Utils.localDateToString(task.dueDate, FormatStyle.SHORT).split(",")
                            .first()
                    )
                    task.note?.title?.let {
                        Icon(
                            modifier = iconsModifier,
                            painter = painterResource(id = R.drawable.description_24px),
                            contentDescription = null
                        )
                    }


                    if (task.subTaskQuantity > 0) {
                        Icon(
                            modifier = iconsModifier,
                            painter = painterResource(id = R.drawable.tenancy_24px),
                            contentDescription = null
                        )
                    }
                    task.reminder?.eventTime?.let {

                        Icon(
                            modifier = iconsModifier,
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null
                        )
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun taskCardPreview() {
    taskCard(
        task = LocalTaskEntity(
            taskTitle = "task name",
            dueDate = LocalDateTime.now(),
            isCompleted = false,
            createDateTime = LocalDateTime.now(),
            categoryIdFk = 1,
            note = Note()
        ),
        changeTaskState = { task, state -> },
        navigateToTaskDetails = {},
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun topBarPreview() {

    topBar(scrollBehavior = null,
        tasksListScreenUiState = TasksListScreenUiState(),
        filter = {},
        activateSearchMode = {},
        showSortByDialog = {},
        manageCategories = {})
}


@Preview(showBackground = true)
@Composable
fun dropDownMenuPreview() {
    dropDownMenu(true, {}, activateSearchMode = {}, showSortByDialog = {}, manageCategories = {})
}


@Preview(showBackground = true)
@Composable
fun TaskListPreview() {
    taskListContent(innerPadding = PaddingValues(20.dp),
        listOf(),
        changeTaskState = { task, state -> },
        navigateToTaskDetails = {})
}