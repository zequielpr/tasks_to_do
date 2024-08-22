package com.kunano.tasks_to_do.tasks_list.presentation

import android.os.Build
import android.window.OnBackInvokedCallback
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBarScrollBehavior
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kunano.tasks_to_do.R
import com.kunano.tasks_to_do.core.utils.navigateBackButton
import com.kunano.tasks_to_do.core.utils.searchBar
import com.kunano.tasks_to_do.tasks_list.presentation.create_task.createTaskBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksListScreen(
    paddingValues: PaddingValues,
    bottomAppBarScrollBehavior: BottomAppBarScrollBehavior,
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
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection)
        ) {
            if (tasksListScreedUiState.isSearchModeActive) {
                TopAppBar(
                    navigationIcon = {
                        navigateBackButton(
                            size = 40.dp,
                            navigateBack = viewModel::deactivateSearchMode
                        )
                    },
                    scrollBehavior = scrollBehavior,
                    title = {
                        searchBar(
                            value = tasksListScreedUiState.searchingString,
                            search = viewModel::search
                        )
                    })
            } else {
                topBar(
                    scrollBehavior = scrollBehavior,
                    tasksListScreenUiState = tasksListScreedUiState,
                    filter = viewModel::filterByTaskCategory,
                    activateSearchMode = viewModel::activateSearchMode
                )
            }


            taskListContent(paddingValues, tasksListScreedUiState.tasksList)
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

    }/*Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { topBar(scrollBehavior = scrollBehavior) },
        floatingActionButton = { floatingActionButton(floatingButtonExpanded) },
        bottomBar = { BottomNavigation(modifier = Modifier.navigationBarsPadding()){} }, //There must be a better solution to this padding problem
        content = { paddingValues -> taskListContent(paddingValues, viewModel) }
    )*/
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBar(
    scrollBehavior: TopAppBarScrollBehavior?,
    tasksListScreenUiState: TasksListScreenUiState,
    filter: (category: String?) -> Unit,
    activateSearchMode: () -> Unit
) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            TaskCategoryCarousel(
                tasksListScreenUiState = tasksListScreenUiState,
                filter = filter
            )
        },
        actions = {

            IconButton(
                onClick = { dropDownMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Localized description"
                )
            }
            dropDownMenu(
                dropDownMenuExpanded, onDismissRequest = { dropDownMenuExpanded = false },
                activateSearchMode
            )
        },
        scrollBehavior = scrollBehavior,
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskCategoryCarousel(
    tasksListScreenUiState: TasksListScreenUiState,
    filter: (category: String?) -> Unit
) {


    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            FilterChip(
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primary),
                shape = ShapeDefaults.Large,
                selected = tasksListScreenUiState.selectedCategory == null,
                onClick = { filter(null) },
                label = { Text(text = stringResource(id = R.string.all)) })
        }

        if (tasksListScreenUiState.tasksList.isNotEmpty()) {
            items(tasksListScreenUiState.tasksList) { categoryLabel ->
                categoryBtn(
                    label = categoryLabel,
                    selectedCategory = tasksListScreenUiState.selectedCategory,
                    selectCategory = filter
                )
            }
        }


    }

}

@Composable
fun categoryBtn(
    label: String,
    selectedCategory: String?,
    selectCategory: (category: String) -> Unit
) {
    FilterChip(
        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primary),
        shape = ShapeDefaults.Large,
        selected = label == selectedCategory,
        onClick = { selectCategory(label) },
        label = { Text(text = label) })
}


@Composable
fun dropDownMenu(
    isExpanded: Boolean,
    onDismissRequest: () -> Unit,
    activateSearchMode: () -> Unit
) {

    DropdownMenu(expanded = isExpanded, onDismissRequest = { onDismissRequest() }) {
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.search)) },
            onClick = activateSearchMode
        )
        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.sort_by)) },
            onClick = { /*TODO*/ })
        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.manage_categories)) },
            onClick = { /*TODO*/ })
        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.delete_task)) },
            onClick = { /*TODO*/ })
    }

}


@Composable
fun floatingActionButton(
    floatingButtonExpanded: Boolean,
    modifier: Modifier,
    showBottomSheet: () -> Unit
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
fun taskListContent(innerPadding: PaddingValues, tasksList: List<String>) {

    println("Recompose")
    //val tasksList by viewModel.tasksList.observeAsState()


    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp, 0.dp),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        items(tasksList) { taskName: String ->
            taskCard(taskName = taskName)
        }
        item {
            Box(modifier = Modifier.height(20.dp))
        }
    }

}


@Composable
fun taskCard(taskName: String) {
    var taskState by rememberSaveable { mutableStateOf(false) }
    Card(modifier = Modifier.fillMaxWidth()) {
        Row {
            Checkbox(checked = taskState, onCheckedChange = { state -> taskState = state })
            Text(
                textDecoration = if (taskState) TextDecoration.LineThrough else null,
                modifier = Modifier.padding(14.dp),
                text = taskName
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun topBarPreview() {

    topBar(
        scrollBehavior = null,
        tasksListScreenUiState = TasksListScreenUiState(),
        filter = {},
        activateSearchMode = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun dropDownMenuPreview() {
    dropDownMenu(true, {}, activateSearchMode = {})
}


@Preview(showBackground = true)
@Composable
fun TaskListPreview() {
    taskListContent(innerPadding = PaddingValues(20.dp), listOf())
}