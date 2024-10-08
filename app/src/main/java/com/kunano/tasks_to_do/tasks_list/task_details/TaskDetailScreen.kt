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
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
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
import androidx.compose.ui.res.painterResource
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
    snackbarHostState: SnackbarHostState,
    bottomAppBarScrollBehavior: BottomAppBarScrollBehavior,
    navigate: (route: Route) -> Unit,
    navigateBack: () -> Unit,
    viewModel: TaskDetailViewModel = hiltViewModel(),
    manageCategoriesViewModel: ManageCategoriesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val taskKey = viewModel.taskKey
    val taskDetailsUiState by viewModel.taskDetail.collectAsStateWithLifecycle(
        TaskDetailsUiState(
            snackBarHostState = snackbarHostState
        )
    )
    val manageCategoriesScreenState by manageCategoriesViewModel.manageCategoriesScreenState.collectAsStateWithLifecycle()
    viewModel.setSnackBarHostState(snackbarHostState)

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
                    showDueDatePicker = viewModel::showDatePicker,
                    showEventAndReminderTimePicker = viewModel::showTimePicker,
                    editNotes = { navigate(Route.NoteScreen(taskKey = taskKey)) },
                    showRemindAtTimePicker = viewModel::showRemindAtTimePicker
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
            dialTimePicker(
                onConfirm = viewModel::setEventAndReminderDateTime,
                onDismiss = viewModel::hideTimePicker,
                timePickerState = taskDetailsUiState.eventTimePickerState,
                onNotReminder = viewModel::deleteTimeReminder
            )
        }

        if (taskDetailsUiState.showReminderTimePicker) {
            dialTimePicker(
                onConfirm = viewModel::setRemindAtDateTime,
                onDismiss = viewModel::hideRemindAtTimePicker,
                timePickerState = taskDetailsUiState.reminderTimePickerState,
                onNotReminder = viewModel::deleteTimeReminder
            )
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
    showDueDatePicker: () -> Unit,
    showEventAndReminderTimePicker: () -> Unit,
    showRemindAtTimePicker: () -> Unit,
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
                showDueDatePicker = showDueDatePicker,
                showEventAndReminderTimePicker = showEventAndReminderTimePicker,
                editNotes = editNotes,
                showRemindAtTimePicker = showRemindAtTimePicker
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


    val focusRequester by remember {
        mutableStateOf(FocusRequester())
    }

    //Focus the last item of the list
    LaunchedEffect(key1 = isTheLastItem) {
        focusRequester.requestFocus()
    }

    //Collect text field state
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

}


@Composable
fun taskFeatures(
    taskDetailsUiState: TaskDetailsUiState,
    showDueDatePicker: () -> Unit,
    showEventAndReminderTimePicker: () -> Unit,
    showRemindAtTimePicker: () -> Unit,
    editNotes: () -> Unit,
    modifier: Modifier = Modifier.padding(10.dp),
) {
    Column() {
        HorizontalDivider()
        Box(modifier = Modifier.clickable { showDueDatePicker() }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
            ) {
                Icon(Icons.Filled.DateRange, contentDescription = null)
                Text(text = stringResource(id = R.string.due_date), modifier.weight(1f))
                Text(text = taskDetailsUiState.dueDate ?: "")

            }
        }
        HorizontalDivider()

        Box(modifier = Modifier.clickable { showEventAndReminderTimePicker() }) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                ) {
                    Icon(Icons.Filled.Notifications, contentDescription = null)
                    Text(
                        text = stringResource(id = R.string.reminder), modifier.weight(1f)
                    )
                    Card {
                        if (taskDetailsUiState.reminderUiState.eventTime == null &&
                            taskDetailsUiState.reminderUiState.reminderTime == null) {
                            Text(modifier = modifier, text = stringResource(id = R.string.no))
                        }else{
                            Text(modifier = modifier, text = stringResource(id = R.string.yes))
                        }
                    }

                }


                taskDetailsUiState.reminderUiState.eventTime?.let {
                    Row(
                        modifier = modifier.clickable { showEventAndReminderTimePicker() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = stringResource(id = R.string.event_time),
                            modifier = Modifier.weight(1f)
                        )
                        Card {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = it
                            )
                        }
                    }
                }

                taskDetailsUiState.reminderUiState.reminderTime?.let {
                    Row(
                        modifier = modifier.clickable { showRemindAtTimePicker() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = stringResource(id = R.string.remind_at),
                            modifier = Modifier.weight(1f)
                        )
                        Card {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = it
                            )
                        }

                    }
                }
            }
        }
        HorizontalDivider()

        Box(modifier = Modifier.clickable { editNotes() }) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                ) {
                    Icon(painter = painterResource(id = R.drawable.description_24px), contentDescription = null)
                    Text(text = stringResource(id = R.string.note), modifier.weight(1f))
                    Card{
                        Text(modifier = modifier, text = stringResource(id = R.string.edit))
                    }

                }
                taskDetailsUiState.attachedNote.title?.let {
                    if (it.isNotBlank()) {
                        Text(
                            modifier = modifier,
                            text = it,
                            style = TextStyle(fontWeight = FontWeight.Medium)
                        )
                    }

                }
                taskDetailsUiState.attachedNote.note?.let {
                    if (it.isNotEmpty()) {
                        Text(modifier = modifier, text = it, maxLines = 5)
                    }

                }


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
        taskDetailsUiState = TaskDetailsUiState(
            attachedNote = AttachedNote("note", "body"),
            dueDate = "10/10/2024",
            reminderUiState = ReminderUiState("10:00", "9:50")
        ),
        deleteSubTask = {},
        subTaskCheckBoxOnClick = { subTaskId, state -> },
        addSubTask = {},
        onTyping = { s, v -> },
        showEventAndReminderTimePicker = {},
        showDueDatePicker = {},
        editNotes = {},
        showRemindAtTimePicker = {}
    )
}



