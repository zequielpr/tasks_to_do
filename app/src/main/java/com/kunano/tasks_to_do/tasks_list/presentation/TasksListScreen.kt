package com.kunano.tasks_to_do.tasks_list.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.kunano.tasks_to_do.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksListScreen(
    viewModel: TaskListViewModel,
    paddingValues: PaddingValues,
    bottomAppBarScrollBehavior: BottomAppBarScrollBehavior
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var floatingButtonExpanded by remember { mutableStateOf(true) }

    //If the top bar is visible the floating button must be expanded
    floatingButtonExpanded = scrollBehavior.state.heightOffset == 0.0f

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection)
        ) {

            topBar(scrollBehavior = scrollBehavior, viewModel)
            taskListContent(paddingValues, viewModel)
        }

        floatingActionButton(
            floatingButtonExpanded = floatingButtonExpanded,
            Modifier
                .align(AbsoluteAlignment.BottomRight)
                .padding(paddingValues)
                .padding(20.dp, 50.dp)
        )
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
fun topBar(scrollBehavior: TopAppBarScrollBehavior?, viewModel: TaskListViewModel) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {TaskCategoryCarousel(viewModel = viewModel)},
        actions = {

            IconButton(
                onClick = { dropDownMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Localized description"
                )
            }
            dropDownMenu(dropDownMenuExpanded) { dropDownMenuExpanded = false }
        },
        scrollBehavior = scrollBehavior,
    )
}


@Composable
fun TaskCategoryCarousel(viewModel: TaskListViewModel) {

    val context = LocalContext.current
    val categoriesList by viewModel.categoriesList.observeAsState()

    var selectedCategory by rememberSaveable {
        mutableStateOf("All")
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        categoriesList?.let { it ->
            items(it.toList()) { categoryLabel ->
                categoryBtn(
                    label = categoryLabel,
                    selectedCategory = selectedCategory
                ) { newlySelectedCategory ->
                    selectedCategory = newlySelectedCategory
                    viewModel.selectTaskCategory(newlySelectedCategory)
                }
            }
        }

    }

}

@Composable
fun categoryBtn(
    label: String,
    selectedCategory: String,
    selectCategory: (category: String) -> Unit
) {
    FilterChip(
        shape = ShapeDefaults.Large,
        selected = label == selectedCategory,
        onClick = { selectCategory(label) },
        label = { Text(text = label) })
}


@Composable
fun dropDownMenu(isExpanded: Boolean, onDismissRequest: () -> Unit) {

    DropdownMenu(expanded = isExpanded, onDismissRequest = { onDismissRequest() }) {
        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.search)) },
            onClick = { /*TODO*/ })
        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.sort_by)) },
            onClick = { /*TODO*/ })
        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.manage_categories)) },
            onClick = { /*TODO*/ })
        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.delete_task)) },
            onClick = { /*TODO*/ })
    }

}


@Composable
fun floatingActionButton(floatingButtonExpanded: Boolean, modifier: Modifier) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        expanded = floatingButtonExpanded,
        onClick = { },
        icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
        text = { Text(text = stringResource(id = R.string.create_task)) },
    )
}


@Composable
fun taskListContent(innerPadding: PaddingValues, viewModel: TaskListViewModel) {

    val tasksList by viewModel.testData.observeAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp, 0.dp),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        tasksList?.let {

            items(it.toList()) { taskName: String ->
                taskCard(taskName = taskName)
            }
        }
    }

}


@Composable
fun taskCard(taskName: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(modifier = Modifier.padding(14.dp), text = taskName)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun topBarPreview() {
    topBar(scrollBehavior = null, TaskListViewModel())
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun dropDownMenuPreview() {
    dropDownMenu(true) {}
}


@Preview(showBackground = true)
@Composable
fun TaskListPreview() {
    taskListContent(innerPadding = PaddingValues(20.dp), viewModel = TaskListViewModel())
}