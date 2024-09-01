package com.kunano.tasks_to_do.tasks_list.task_details

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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material.icons.sharp.Menu
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kunano.tasks_to_do.R
import com.kunano.tasks_to_do.core.data.model.entities.LocalCategoryEntity
import com.kunano.tasks_to_do.core.data.model.entities.LocalSubTaskEntity
import com.kunano.tasks_to_do.core.utils.basicDropDownMenu
import com.kunano.tasks_to_do.core.utils.categoryAssistChip
import com.kunano.tasks_to_do.core.utils.createCategoryDialog
import com.kunano.tasks_to_do.core.utils.customBasicTextField
import com.kunano.tasks_to_do.core.utils.datePicker
import com.kunano.tasks_to_do.core.utils.dialTimePicker
import com.kunano.tasks_to_do.core.utils.navigateBackButton
import com.kunano.tasks_to_do.tasks_list.manage_category.ManageCategoriesScreenState
import com.kunano.tasks_to_do.tasks_list.manage_category.ManageCategoriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    contentPadding: PaddingValues,
    bottomAppBarScrollBehavior: BottomAppBarScrollBehavior,
    navigate: (route: Route) -> Unit,
    navigateBack: () -> Unit,
    viewModel: TaskDetailViewModel = hiltViewModel(),
    manageCategoriesViewModel: ManageCategoriesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val taskKey = viewModel.taskKey
    val taskDetailsUiState by viewModel.taskDetail.collectAsStateWithLifecycle()
    val manageCategoriesScreenState by manageCategoriesViewModel.manageCategoriesScreenState.collectAsStateWithLifecycle()

    //Set the the recently created category
    manageCategoriesViewModel.newCategoryReceiver = {
        viewModel.updateTaskCategory(it)
    }

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
            selectAction = viewModel::selectDropDownMenuAction,
            setCategory = viewModel::updateTaskCategory,
            showCreateCategoryDialog = manageCategoriesViewModel::showAddCategoryDialog
        )
        LazyColumn(
            modifier = modifier
                .padding(20.dp, 0.dp)
                .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
        ) {
            item {
                customBasicTextField(
                    modifier = modifier,
                    value = taskDetailsUiState.taskTitle,
                    hint = R.string.task_details,
                    onValueChange = viewModel::updateTaskTitle
                )



                taskDetailBody(
                    contentPadding = contentPadding,
                    taskDetailsUiState = taskDetailsUiState,
                    addSubTask = viewModel::addNewSubTaskField,
                    deleteSubTask = viewModel::deleteSubTaskField,
                    subTaskCheckBoxOnClick = viewModel::updateSubTaskState,
                    onTyping = viewModel::onSubTaskInputChanges,
                    pickDueDate = viewModel::showDatePicker,
                    setReminder = viewModel::showTimePicker,
                    editNotes = { navigate(Route.NoteScreen(taskKey = taskKey)) }
                )
            }
        }


        if (manageCategoriesScreenState.showCreateCategoryDialog) {
            createCategoryDialog(
                manageCategoriesScreenState = manageCategoriesScreenState,
                onValueChange = manageCategoriesViewModel::onChangeCategoryName,
                onDismiss = manageCategoriesViewModel::hideCreateOrUpdateTaskDialog,
                createCategory = manageCategoriesViewModel::saveChanges
            )
        }


        if (taskDetailsUiState.showDueDatePicker) {
            datePicker(
                selectedDate = taskDetailsUiState.dueDateLong,
                onDismiss = viewModel::hideDatePicker,
                pickDate = viewModel::setDueDate
            )
        }

        if (taskDetailsUiState.showTimePicker) {
            dialTimePicker(onConfirm = {}, onDismiss = viewModel::hideTimePicker)
        }

        SnackbarHost(hostState = taskDetailsUiState.snackBarHostState)
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
    setCategory: (category: LocalCategoryEntity?) -> Unit,
    showCreateCategoryDialog: () -> Unit

) {
    TopAppBar(
        scrollBehavior = topAppBarScrollBehavior,
        title = {
            categoryAssistChip(
                categoriesList = manageCategoriesScreenState.categoryList,
                selectedCategoryName = taskDetailsUiState.category,
                trailingIcon = Icons.Filled.ArrowDropDown,
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
    contentPadding: PaddingValues,
    taskDetailsUiState: TaskDetailsUiState,
    subTaskCheckBoxOnClick: (subTask: LocalSubTaskEntity, state: Boolean) -> Unit,
    deleteSubTask: (subTask: LocalSubTaskEntity) -> Unit,
    addSubTask: () -> Unit,
    pickDueDate: () -> Unit,
    setReminder: () -> Unit,
    editNotes: () -> Unit,
    onTyping: (subTask: LocalSubTaskEntity, value: String) -> Unit,
    modifier: Modifier = Modifier.padding(contentPadding)
) {

    Box(
        modifier = modifier
    ) {
        Column {
            subTasks(
                taskDetailsUiState = taskDetailsUiState,
                subTaskCheckBoxOnClick = subTaskCheckBoxOnClick,
                deleteSubTask = deleteSubTask,
                onTyping = onTyping,
                addSubTask = addSubTask
            )
            taskFeatures(
                taskDetailsUiState = taskDetailsUiState,
                pickDueDate = pickDueDate,
                setReminder = setReminder,
                editNotes = editNotes,
            )
        }
    }
}

@Composable
fun subTasks(
    taskDetailsUiState: TaskDetailsUiState,
    subTaskCheckBoxOnClick: (subTask: LocalSubTaskEntity, state: Boolean) -> Unit,
    deleteSubTask: (subTask: LocalSubTaskEntity) -> Unit,
    addSubTask: () -> Unit,
    onTyping: (subTask: LocalSubTaskEntity, value: String) -> Unit,
    modifier: Modifier = Modifier
        .padding(0.dp, 10.dp)
        .fillMaxWidth()
) {
    Column {
        var index = 0
        taskDetailsUiState.subTasksInputInputState.fastForEach {
            key(it.subTaskId) {
                println("sub task's input: $index")
                index++
                subTaskCard(
                    subTask = it,
                    isTheLastItem = index == taskDetailsUiState.subTasksInputInputState.size, //Focus the last item
                    subTaskCheckBoxOnClick = subTaskCheckBoxOnClick,
                    deleteSubTask = deleteSubTask,
                    onTyping = onTyping
                )
            }

        }

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
    subTask: LocalSubTaskEntity,
    isTheLastItem: Boolean,
    subTaskCheckBoxOnClick: (subTask: LocalSubTaskEntity, state: Boolean) -> Unit,
    deleteSubTask: (subTask: LocalSubTaskEntity) -> Unit,
    onTyping: (subTask: LocalSubTaskEntity, value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val mutableInteractionSource by remember { mutableStateOf(MutableInteractionSource()) }

    // Create a FocusRequester object
    val focusRequester = remember { FocusRequester() }


    //Focus on the last item

    val isTextFieldFocused by mutableInteractionSource.collectIsFocusedAsState()
    val textStyle = if (subTask.isCompleted) TextStyle(
        textDecoration = TextDecoration.LineThrough,
        color = Color.Gray
    ) else null

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = subTask.isCompleted,
            onCheckedChange = { subTaskCheckBoxOnClick(subTask, it) })

        customBasicTextField(
            textStyle = textStyle,
            modifier = modifier
                .weight(1f)
                .focusRequester(focusRequester),
            mutableInteractionSource = mutableInteractionSource,
            value = subTask.title ?: "",
            hint = R.string.input_sub_task_name,
            onValueChange = { onTyping(subTask, it) })

        IconButton(
            onClick = { deleteSubTask(subTask) },
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
    taskDetailsUiState: TaskDetailsUiState,
    pickDueDate: () -> Unit,
    setReminder: () -> Unit,
    editNotes: () -> Unit,
    modifier: Modifier = Modifier.padding(10.dp),
) {
    Column() {
        HorizontalDivider()
        Box(modifier = Modifier.clickable { pickDueDate() }){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier) {
                Icon(Icons.Filled.DateRange, contentDescription = null)
                Text(text = taskDetailsUiState.dueDate?:"", modifier.weight(1f))
                Text(text = stringResource(id = R.string.edit))

            }
        }
        HorizontalDivider()

       Box(modifier = Modifier.clickable { setReminder() }){
           Row(
               verticalAlignment = Alignment.CenterVertically,
               modifier = modifier) {
               Icon(Icons.Filled.Notifications, contentDescription = null)
               Text(text = stringResource(id = R.string.time_and_reminder), modifier.weight(1f))
               Text(text = "event time")

           }
       }
        HorizontalDivider()

        Box(modifier = Modifier.clickable { editNotes() }){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier) {
                Icon(Icons.Filled.Menu, contentDescription = null)
                Text(text = stringResource(id = R.string.notes), modifier.weight(1f))
                Text(text = stringResource(id = R.string.edit))

            }
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
        deleteSubTask = {},
        subTaskCheckBoxOnClick = { subTaskId, state -> },
        addSubTask = {},
        onTyping = { s, v -> },
        setReminder = {},
        pickDueDate = {},
        editNotes = {}
    )
}



