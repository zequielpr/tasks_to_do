package com.kunano.tasks_to_do.tasks_list.presentation.create_task

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kunano.tasks_to_do.R
import com.kunano.tasks_to_do.core.utils.CategoriesDropDownMenu
import com.kunano.tasks_to_do.core.utils.Utils
import com.kunano.tasks_to_do.core.utils.createCategoryDialog
import com.kunano.tasks_to_do.core.utils.dateModifier
import com.kunano.tasks_to_do.core.utils.showToast
import com.kunano.tasks_to_do.tasks_list.presentation.states.CreateCategoryUiState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun createTaskBottomSheet(
    onDismiss: () -> Unit, viewModel: CreateTaskViewModel = hiltViewModel()
) {

    val createTaskUiState by viewModel.createTaskUiState.collectAsStateWithLifecycle()
    val createCategoryUiState by viewModel.createCategoryUiState.collectAsStateWithLifecycle()
    val showIntroduceTaskNameToastState by viewModel.showIntroduceTaskNameToast.collectAsStateWithLifecycle(
        initialValue = false
    )

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = bottomSheetState, onDismissRequest = onDismiss,
    ) {
        if (showIntroduceTaskNameToastState) {
            showToast(message = R.string.introduce_task_name)
            viewModel.showIntroduceTaskNameToast(false)
        }

        bottomSheetContent(
            createTaskUiState = createTaskUiState,
            createCategoryUiState = createCategoryUiState,
            onChangeName = viewModel::onChangeName,

            showCategoryDropDownMenu = viewModel::showCategoryDropDownMenu,
            showDatePicker = viewModel::showDatePicker,
            hideDatePicker = viewModel::hideDatePicker,
            setDate = viewModel::setDate,
            hideDropDownMenu = viewModel::hideCategoryDropDownMenu,
            selectCategory = viewModel::selectTaskCategory,
            createTask = viewModel::createTask,
            onCategoryNameChange = viewModel::onCategoryNameChange,
            showCreateCategoryDialog = viewModel::showCreateCategoryDialog,
            onCategoryDialogDismiss = viewModel::hideCreateCategoryDialog,
            createCategory = viewModel::createCategory,

            )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun bottomSheetContent(
    createTaskUiState: CreateTaskUiState,
    createCategoryUiState: CreateCategoryUiState,
    onChangeName: (newValue: String) -> Unit,
    showCategoryDropDownMenu: () -> Unit,
    showDatePicker: () -> Unit,
    hideDatePicker: () -> Unit,
    setDate: (dateInMilli: Long?) -> Unit,
    hideDropDownMenu: () -> Unit,
    selectCategory: (category: String?) -> Unit,
    createTask: () -> Unit,
    onCategoryNameChange: (category: String) -> Unit,
    showCreateCategoryDialog: () -> Unit,
    onCategoryDialogDismiss: () -> Unit,
    createCategory: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(10.dp)
            .imePadding()
    ) {

        OutlinedTextField(
            label = { Text(text = stringResource(id = R.string.task_name)) },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            value = createTaskUiState.taskName,
            onValueChange = onChangeName
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                createCategoryDialog(
                    createCategoryUiState = createCategoryUiState,
                    onValueChange = onCategoryNameChange,
                    onDismiss = onCategoryDialogDismiss,
                    createCategory = createCategory
                )

                CategoriesDropDownMenu(
                    createTaskUiState.categoryList,
                    createTaskUiState.showCategoriesDropDownMenu,
                    onDismiss = hideDropDownMenu,
                    selectItem = selectCategory,
                    createCategory = showCreateCategoryDialog
                )

                AssistChip(
                    shape = ShapeDefaults.Medium,
                    onClick = showCategoryDropDownMenu,
                    label = {
                        Text(
                            text = createTaskUiState.selectedCategoryInBottomSheet
                                ?: stringResource(
                                    id = R.string.no_category
                                )
                        )
                    })


                dateModifier(
                    datePickerToBeShown = createTaskUiState.showDatePicker,
                    selectedDateInMilliseconds = createTaskUiState.selectedDateInMilliseconds,
                    selectedDay = createTaskUiState.selectedDayOfMonth,
                    showDatePicker = showDatePicker,
                    hideDatePicker = hideDatePicker,
                    setDate = setDate
                )
            }
            ElevatedButton(onClick = createTask) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun datePickerPreview() {
    dateModifier(datePickerToBeShown = true,
        selectedDateInMilliseconds = Utils.getCurrentTimeInMilliseconds(),
        selectedDay = 10,
        showDatePicker = { /*TODO*/ },
        hideDatePicker = { /*TODO*/ },
        setDate = {})
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun createTaskBottomSheetPreview() {
    bottomSheetContent(createTaskUiState = CreateTaskUiState(),
        createCategoryUiState = CreateCategoryUiState(),
        onChangeName = {},
        showCategoryDropDownMenu = { /*TODO*/ },
        showDatePicker = { /*TODO*/ },
        hideDatePicker = { /*TODO*/ },
        setDate = {},
        hideDropDownMenu = { },
        selectCategory = {},
        createTask = {},
        onCategoryNameChange = {},
        showCreateCategoryDialog = {},
        onCategoryDialogDismiss = {},
        createCategory = {}

    )
}
