package com.kunano.tasks_to_do.tasks_list.presentation.task_details

import Route
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material.icons.sharp.Menu
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kunano.tasks_to_do.R
import com.kunano.tasks_to_do.core.utils.basicDropDownMenu
import com.kunano.tasks_to_do.core.utils.categoryAssistChip
import com.kunano.tasks_to_do.core.utils.customBasicTextField
import com.kunano.tasks_to_do.core.utils.navigateBackButton
import com.kunano.tasks_to_do.tasks_list.presentation.manage_category.ManageCategoriesScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskKey: String,
    contentPadding: PaddingValues,
    bottomAppBarScrollBehavior: BottomAppBarScrollBehavior,
    navigate: ((route: Route) -> Unit)? = null,
    navigateBack: () -> Unit,
    viewModel: TaskDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val taskDetailsUiState by viewModel.taskDetail.collectAsStateWithLifecycle()
    val manageCategoriesScreenState by viewModel.manageCategoriesScreenState.collectAsStateWithLifecycle()


    val topAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Column(
        modifier
            .fillMaxSize()
    ) {
        taskDetailTopAppBar(
            topAppBarScrollBehavior = topAppBarScrollBehavior,
            taskDetailsUiState = taskDetailsUiState,
            manageCategoriesScreenState = manageCategoriesScreenState,
            navigateBack = navigateBack,
            selectAction = {},
            setCategory = {},
            showCreateCategoryDialog = {}
        )
        LazyColumn(
            modifier = modifier
                .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
        ) {
            item {
                Text(
                    modifier = Modifier.padding(20.dp, 0.dp),
                    text = "taskTitle",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )


                taskDetailBody(
                    contentPadding = contentPadding,
                    taskDetailsUiState = taskDetailsUiState,
                    manageCategoriesScreenState = manageCategoriesScreenState,
                    addSubTask = viewModel::addNewSubTaskField,
                    deleteSubTask = viewModel::deleteSubTaskField,
                    subTaskCheckBoxOnClick = viewModel::updateSubTaskState,
                    onTyping = viewModel::onSubTaskInputChanges
                )
            }
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun taskDetailTopAppBar(
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    taskDetailsUiState: TaskDetailsUiState,
    manageCategoriesScreenState: ManageCategoriesScreenState,
    navigateBack: () -> Unit,
    selectAction: (action: Int) -> Unit,
    setCategory: (category: String?) -> Unit,
    showCreateCategoryDialog: () -> Unit

) {
    TopAppBar(
        scrollBehavior = topAppBarScrollBehavior,
        title = {
            categoryAssistChip(
                categoriesList = manageCategoriesScreenState.categoryList,
                selectedCategory = taskDetailsUiState.category,
                selectItem = setCategory,
                showCreateCategoryDialog = showCreateCategoryDialog
            )
        },
        navigationIcon = { navigateBackButton(navigateBack = navigateBack) },
        actions = {
            basicDropDownMenu(
                optionsList = taskDetailsUiState.dropDownMenuOptions,
                selectOption = selectAction
            )
        })
}

@Composable
fun taskDetailBody(
    manageCategoriesScreenState: ManageCategoriesScreenState,
    contentPadding: PaddingValues,
    taskDetailsUiState: TaskDetailsUiState,
    subTaskCheckBoxOnClick: (subTaskId: String, state: Boolean) -> Unit,
    deleteSubTask: (subTaskInputState: SubTaskInputState) -> Unit,
    addSubTask: () -> Unit,
    onTyping: (subTaskInputState: SubTaskInputState, value: String) -> Unit,
    modifier: Modifier = Modifier.padding(contentPadding)
) {

    Box(
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(20.dp, 0.dp)) {
            subTasks(
                taskDetailsUiState = taskDetailsUiState,
                subTaskCheckBoxOnClick = subTaskCheckBoxOnClick,
                deleteSubTask = deleteSubTask,
                onTyping = onTyping,
                addSubTask = addSubTask
            )
            taskFeatures(taskDetailsUiState = taskDetailsUiState)
        }
    }
}

@Composable
fun subTasks(
    taskDetailsUiState: TaskDetailsUiState,
    subTaskCheckBoxOnClick: (subTaskId: String, state: Boolean) -> Unit,
    deleteSubTask: (subTaskInputState: SubTaskInputState) -> Unit,
    addSubTask: () -> Unit,
    onTyping: (subTaskInputState: SubTaskInputState, value: String) -> Unit,
    modifier: Modifier = Modifier
        .padding(0.dp, 10.dp)
        .fillMaxWidth()
) {
    Column {
        var index = 0
        taskDetailsUiState.subTasksInputInputState.toList().fastForEach {
            key(it.subTaskId) {
                println("sub task's input: $index")
                index++
                subTaskCard(
                    subTaskInputState = it,
                    isTheLastItem = index == taskDetailsUiState.subTasksInputInputState.size, //Focus the last item
                    subTaskCheckBoxOnClick = subTaskCheckBoxOnClick,
                    deleteSubTask = deleteSubTask,
                    onTyping = onTyping
                )
            }

        }
        taskDetailsUiState.subTasks.fastForEach {
            /* subTaskCard(
                subTask = it,
                 isTheLastItem = false,
                 markSubtaskAsDone = {},
                 deleteSubTask = {},
                 onTyping = onTyping
             )*/
        }
        /*Card(shape = RoundedCornerShape(20.dp)) {
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 190.dp, min = 0.dp)
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(taskDetailsUiState.subTasksInput) {
                    subTaskCard(
                        subtaskName = "sub $it", isSubTaskDone = false,
                        markSubtaskAsDone = markSubtaskAsDone, deleteSubTask = deleteSubTask
                    )
                }

                items(0) {
                    subTaskCard(
                        subtaskName = "sub $it", isSubTaskDone = false,
                        markSubtaskAsDone = markSubtaskAsDone, deleteSubTask = deleteSubTask
                    )
                }
            }
        }*/


        Box(modifier = modifier.fillMaxWidth()) {
            Row(
                modifier = modifier.clickable { addSubTask() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    modifier = Modifier.padding(10.dp, 0.dp),
                    text = stringResource(id = R.string.add_sub_task),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }

}


@Composable
fun subTaskCard(
    subTaskInputState: SubTaskInputState,
    isTheLastItem: Boolean,
    subTaskCheckBoxOnClick: (subTaskId: String, state: Boolean) -> Unit,
    deleteSubTask: (subTaskInputState: SubTaskInputState) -> Unit,
    onTyping: (subTaskInputState: SubTaskInputState, value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val mutableInteractionSource by remember { mutableStateOf(MutableInteractionSource()) }

    // Create a FocusRequester object
    val focusRequester = remember { FocusRequester() }


    //Focus on the last item

    val isTextFieldFocused by mutableInteractionSource.collectIsFocusedAsState()

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = subTaskInputState.isSubTaskDone,
            onCheckedChange = { subTaskCheckBoxOnClick(subTaskInputState.subTaskId, it) })

        customBasicTextField(
            modifier = modifier
                .weight(1f)
                .focusRequester(focusRequester),
            mutableInteractionSource = mutableInteractionSource,
            value = subTaskInputState.subTaskName ?: "",
            hint = R.string.input_sub_task_name,
            onValueChange = { onTyping(subTaskInputState, it) })

        IconButton(
            onClick = { deleteSubTask(subTaskInputState) },
            enabled = isTextFieldFocused
        ) {
            if (isTextFieldFocused) {
                Icon(Icons.Sharp.Clear, contentDescription = null)
            } else {
                Icon(Icons.Sharp.Menu, contentDescription = null)
            }
        }
    }


    //Focused the text as son as it appears on the screen
    if (isTheLastItem) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}


@Composable
fun taskFeatures(
    modifier: Modifier = Modifier.padding(24.dp),
    taskDetailsUiState: TaskDetailsUiState
) {
    Column() {
        HorizontalDivider()
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.DateRange, contentDescription = null)
            Text(text = stringResource(id = R.string.due_date), modifier.weight(1f))
            Text(text = "dueDate")

        }
        HorizontalDivider()

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Notifications, contentDescription = null)
            Text(text = stringResource(id = R.string.time_and_reminder), modifier.weight(1f))
            Text(text = "event time")

        }
        HorizontalDivider()

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Menu, contentDescription = null)
            Text(text = stringResource(id = R.string.notes), modifier.weight(1f))
            Text(text = stringResource(id = R.string.edit))

        }

        HorizontalDivider()
    }
}


@Preview(showBackground = true)
@Composable
fun taskBodyPreview() {
    taskDetailBody(
        contentPadding = PaddingValues(20.dp),
        taskDetailsUiState = TaskDetailsUiState(),
        manageCategoriesScreenState = ManageCategoriesScreenState(),
        deleteSubTask = {},
        subTaskCheckBoxOnClick = {subTaskId, state ->},
        addSubTask = {},
        onTyping = { s, v -> }
    )
}


@Preview(showBackground = true)
@Composable
fun taskFeaturesPreview() {
    taskFeatures(taskDetailsUiState = TaskDetailsUiState())
}


