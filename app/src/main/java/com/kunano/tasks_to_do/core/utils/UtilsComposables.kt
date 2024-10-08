package com.kunano.tasks_to_do.core.utils

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import com.kunano.tasks_to_do.R
import com.kunano.tasks_to_do.core.data.model.entities.LocalCategoryEntity
import com.kunano.tasks_to_do.tasks_list.manage_category.ManageCategoriesScreenState
import java.util.Calendar


@Composable
fun basicDropDownMenu(
    optionsList: List<Int>,
    selectOption: (option: Int) -> Unit,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    IconButton(onClick = { isMenuExpanded = true }) {
        Icon(Icons.Filled.MoreVert, contentDescription = null)
    }

    DropdownMenu(expanded = isMenuExpanded, onDismissRequest = { isMenuExpanded = false }) {
        optionsList.fastForEach {
            DropdownMenuItem(text = { Text(text = stringResource(id = it)) }, onClick = {
                selectOption(it)
                isMenuExpanded = false
            })
        }
    }

}


@Composable
fun dateModifier(
    datePickerToBeShown: Boolean,
    selectedDateInMilliseconds: Long?,
    selectedDay: Int,
    showDatePicker: () -> Unit,
    hideDatePicker: () -> Unit,
    setDate: (date: Long?) -> Unit,
    modifier: Modifier = Modifier
) {

    Box {

        IconButton(
            modifier = Modifier
                .size(44.dp)
                .align(Alignment.Center), onClick = showDatePicker
        ) {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center),
                imageVector = Icons.Filled.DateRange,
                contentDescription = null
            )
            Text(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .padding(0.dp, 5.dp),
                fontSize = TextUnit(13f, TextUnitType.Sp),
                text = selectedDay.toString()
            )
        }
    }

    if (datePickerToBeShown) {
        datePicker(selectedDateInMilliseconds,
            onDismiss = hideDatePicker,
            pickDate = { dateInMilli -> setDate(dateInMilli) })
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun datePicker(
    selectedDate: Long?, onDismiss: () -> Unit, pickDate: (dateInMilli: Long?) -> Unit

) {
    val datePickerState = rememberDatePickerState()
    datePickerState.selectedDateMillis = selectedDate

    DatePickerDialog(confirmButton = {
        ElevatedButton(onClick = { pickDate(datePickerState.selectedDateMillis) }) {
            Text(
                text = stringResource(
                    id = R.string.confirm
                )
            )
        }
    }, dismissButton = {
        ElevatedButton(onClick = onDismiss) {
            Text(text = stringResource(id = R.string.cancel))
        }
    }, onDismissRequest = onDismiss
    ) {
        DatePicker(state = datePickerState)

    }

}


@Composable
fun categoryAssistChip(
    categoriesList: List<LocalCategoryEntity>,
    trailingIcon: (ImageVector)? = null,
    selectedCategoryName: String?,
    selectItem: (categoryId: LocalCategoryEntity?) -> Unit,
    showCreateCategoryDialog: () -> Unit
) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }
    Box() {
        CategoriesDropDownMenu(
            itemsList = categoriesList,
            menuExpanded = menuExpanded,
            onDismiss = { menuExpanded = false },
            selectItem = selectItem,
            createCategory = showCreateCategoryDialog
        )

        AssistChip(trailingIcon = {
            if (trailingIcon != null) Icon(
                imageVector = trailingIcon, contentDescription = null
            )
        }, shape = ShapeDefaults.Medium, onClick = { menuExpanded = true }, label = {
            Text(
                text = selectedCategoryName ?: stringResource(
                    id = R.string.no_category
                )
            )
        })
    }
}


@Composable
fun CategoriesDropDownMenu(
    itemsList: List<LocalCategoryEntity>,
    menuExpanded: Boolean,
    onDismiss: () -> Unit,
    selectItem: (category: LocalCategoryEntity?) -> Unit,
    createCategory: () -> Unit
) {
    DropdownMenu(
        modifier = Modifier.heightIn(max = 250.dp),
        properties = PopupProperties(focusable = false), //prevents the text field from losing its focus
        expanded = menuExpanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(text = {
            Text(
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(id = R.string.no_category)
            )
        }, onClick = {
            selectItem(null)
            onDismiss()

        })

        itemsList.fastForEach {
            DropdownMenuItem(text = { Text(text = it.categoryName) }, onClick = {
                selectItem(it)
                onDismiss()

            })
        }


        DropdownMenuItem(text = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    color = MaterialTheme.colorScheme.primary,
                    text = stringResource(id = R.string.create_category)
                )
            }
        }, onClick = {
            createCategory()
            onDismiss()

        })


    }
}

@Composable
fun createCategoryDialog(
    manageCategoriesScreenState: ManageCategoriesScreenState,
    onValueChange: (category: String) -> Unit,
    onDismiss: () -> Unit,
    buttonTitle: (Int)? = R.string.save,
    createCategory: () -> Unit
) {
    if (manageCategoriesScreenState.showCreateCategoryDialog) {
        Dialog(
            onDismissRequest = onDismiss
        ) {

            createCategoryDialogContent(
                createCategoryUiState = manageCategoriesScreenState,
                onValueChange = onValueChange,
                buttonTitle = buttonTitle!!,
                createCategory = createCategory
            )
        }
    }

}


@Composable
fun createCategoryDialogContent(
    createCategoryUiState: ManageCategoriesScreenState,
    onValueChange: (category: String) -> Unit,
    createCategory: () -> Unit,
    buttonTitle: Int,
    modifier: Modifier = Modifier.fillMaxWidth()
) {

    Card(
        modifier = modifier
            .height(200.dp)
            .padding(10.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = modifier.padding(10.dp)
        ) {
            OutlinedTextField(
                label = { Text(text = stringResource(id = R.string.category_name)) },
                modifier = modifier,
                value = createCategoryUiState.categoryName,
                onValueChange = onValueChange
            )
            if (createCategoryUiState.showErrorMessage) {
                Text(
                    style = TextStyle(color = Color.Red),
                    text = stringResource(id = R.string.introduce_category_name)
                )
            }
            ElevatedButton(modifier = Modifier
                .align(alignment = Alignment.End)
                .padding(0.dp, 10.dp),
                onClick = {
                    createCategory()
                }) {
                Text(text = stringResource(id = buttonTitle!!))
            }
        }

    }


}


@Composable
fun showToast(message: Int) {
    val ctx = LocalContext.current
    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
}


@Composable
fun searchBar(
    value: String, search: (value: String) -> Unit, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(size = 40.dp),
        colors = CardColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            disabledContentColor = Color.Black,
            disabledContainerColor = Color.Black
        ),
        border = BorderStroke(color = Color.Gray, width = 1.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            //Basic textField
            customBasicTextField(
                hint = R.string.search,
                modifier = Modifier
                    .padding(14.dp, 8.dp)
                    .weight(1f),
                value = value,
                onValueChange = search
            )

            Box {
                if (value.isEmpty()) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Default.Search,
                            tint = Color.Gray,
                            contentDescription = stringResource(id = R.string.search)
                        )
                    }
                } else {
                    IconButton(onClick = { search("") }) {
                        Icon(
                            Icons.Default.Clear,
                            tint = Color.Gray,
                            contentDescription = stringResource(id = R.string.search)
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun customBasicTextField(
    textStyle: TextStyle? = null,
    singleLine: Boolean? = null,
    modifier: Modifier,
    value: String,
    hint: Int,
    mutableInteractionSource: (MutableInteractionSource)? = MutableInteractionSource(),
    onValueChange: (value: String) -> Unit,

    ) {

    BasicTextField(modifier = modifier,
        textStyle = textStyle ?: TextStyle(fontSize = 20.sp),
        singleLine = singleLine ?: true,
        value = value,
        onValueChange = onValueChange,
        interactionSource = mutableInteractionSource,
        decorationBox = {
            if (value.isEmpty()) {
                Text(
                    style = TextStyle(color = Color.Gray, fontSize = 20.sp),
                    text = stringResource(id = hint) + "..."
                )
            }
            it()
        })
}


@Composable
fun navigateBackButton(
    size: Dp = 40.dp, navigateBack: () -> Unit
) {
    IconButton(onClick = navigateBack) {
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            modifier = Modifier.size(size),
            contentDescription = stringResource(id = R.string.back),

            )
    }
}


@Composable
fun sortByDialog(
    title: Int,
    selectedOption: Int,
    options: List<Int>,
    selectOption: (option: Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier.padding(20.dp)
) {

    var radioButtonState by remember { mutableIntStateOf(selectedOption) }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = modifier) {
            Column(horizontalAlignment = (Alignment.CenterHorizontally), modifier = modifier) {
                Text(
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    text = stringResource(id = title)
                )
                HorizontalDivider(modifier = Modifier.padding(0.dp, 10.dp))
                LazyColumn {
                    items(options) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = it == radioButtonState,
                                onClick = { radioButtonState = it })
                            Text(modifier = Modifier
                                .clickable { radioButtonState = it }
                                .weight(1f),
                                text = stringResource(id = it))
                        }
                    }
                }
                TextButton(modifier = Modifier.align(Alignment.End),
                    onClick = { selectOption(radioButtonState) }) {
                    Text(text = stringResource(id = R.string.select))
                }
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun sortByDialogPreview() {
    val options = listOf(
        R.string.due_date_and_time,
        R.string.task_create_time_latest_at_the_top,
        R.string.task_create_time_latest_at_the_bottom,
        R.string.alphabetical_a_z,
        R.string.alphabetical_z_a
    )

    sortByDialog(title = R.string.sort_tasks_by,
        selectedOption = R.string.due_date_and_time,
        options = options,
        selectOption = {},
        onDismiss = {})
}

@Composable
fun basicAlertDialog(
    confirmButtonText: Int,
    dismissButtonText: Int,
    title: Int,
    body: Int,
    confirm: () -> Unit,
    dismiss: () -> Unit,
) {


    AlertDialog(title = { Text(text = stringResource(id = title)) },
        text = { Text(text = stringResource(id = body)) },

        onDismissRequest = dismiss,
        confirmButton = {
            TextButton(onClick = confirm) {
                Text(text = stringResource(id = confirmButtonText))
            }
        },
        dismissButton = {
            TextButton(onClick = dismiss) {
                Text(text = stringResource(id = dismissButtonText))
            }
        })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dialTimePicker(
    onConfirm: (timePickerState: TimePickerState) -> Unit,
    onDismiss: () -> Unit,
    onNotReminder: () -> Unit,
    timePickerState: TimePickerState
) {
    var timePickerState by remember {
        mutableStateOf(timePickerState)
    }
    val horizontalArrangement = Arrangement.spacedBy(14.dp)
    AlertDialog(onDismissRequest = { /*TODO*/ }, confirmButton = {
        ElevatedButton(onClick = { onConfirm(timePickerState) }) {
            Text(text = stringResource(id = R.string.confirm))
        }
    }, dismissButton = {
        ElevatedButton(onClick = onDismiss) {
            Text(text = stringResource(id = R.string.cancel))
        }
    }, text = {
        Column(horizontalAlignment = Alignment.End) {
            TimePicker(
                state = timePickerState,
            )

            Row(horizontalArrangement = horizontalArrangement) {


                ElevatedButton(onClick = {
                    timePickerState = TimePickerState (initialHour =
                        13, initialMinute = 0, is24Hour = true)
                }) {
                    Text(text = stringResource(id = R.string.one_pm))
                }


                ElevatedButton(onClick = {  timePickerState = TimePickerState (initialHour =
                0, initialMinute = 0, is24Hour = true) }) {
                    Text(text = stringResource(id = R.string.twelve_pm))
                }
            }

            Row(horizontalArrangement = horizontalArrangement, modifier = Modifier.padding(top = 8.dp)) {

                ElevatedButton(onClick = onNotReminder) {
                    Text(text = stringResource(id = R.string.not_reminder))
                }
                ElevatedButton(onClick = {  timePickerState = TimePickerState (initialHour =
                19, initialMinute = 0, is24Hour = true)}) {
                    Text(text = stringResource(id = R.string.seven_pm))
                }

            }

            Box(modifier = Modifier.padding(vertical = 10.dp))
            HorizontalDivider()


        }
    })

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun timePickerPreview() {
    dialTimePicker(
        onConfirm = { /*TODO*/ },
        onDismiss = {},
        timePickerState = TimePickerState(initialHour = 0, initialMinute = 0, is24Hour = true),
        onNotReminder = {}
    )
}


@Preview(showBackground = true)
@Composable
fun basicAlertDialogPreview() {
    basicAlertDialog(
        confirmButtonText = R.string.confirm,
        dismissButtonText = R.string.cancel,
        confirm = {},
        dismiss = {},
        title = R.string.delete,
        body = R.string.delete_this_category
    )
}


@Preview(showBackground = true)
@Composable
fun searchBarPreview() {
    searchBar(value = "", search = {})
}


@Preview(showBackground = true)
@Composable
fun datePickerPreview() {
    dateModifier(datePickerToBeShown = false,
        selectedDateInMilliseconds = Utils.getCurrentTimeInMilliseconds(),
        selectedDay = 10,
        showDatePicker = { /*TODO*/ },
        hideDatePicker = { /*TODO*/ },
        setDate = {})
}


@Preview(showBackground = true)
@Composable
fun createCategoryDialogPreview() {
    createCategoryDialogContent(ManageCategoriesScreenState(),
        createCategory = {},
        buttonTitle = R.string.save,
        onValueChange = {})
}

