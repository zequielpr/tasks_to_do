package com.kunano.tasks_to_do.core.utils

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import com.kunano.tasks_to_do.R
import com.kunano.tasks_to_do.tasks_list.presentation.states.CreateCategoryUiState

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
            pickDate = { dateInMilli -> setDate(dateInMilli) }
        )
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
fun CategoriesDropDownMenu(
    itemsList: List<String>,
    menuExpanded: Boolean,
    onDismiss: () -> Unit,
    selectItem: (category: String?) -> Unit,
    createCategory: () -> Unit
) {
    DropdownMenu(
        modifier = Modifier.heightIn(max = 250.dp),
        properties = PopupProperties(focusable = false), //prevents the text field from losing its focus
        expanded = menuExpanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    color = MaterialTheme.colorScheme.primary,
                    text = stringResource(id = R.string.no_category)
                )
            },
            onClick = {
                selectItem(null)
                onDismiss()

            })

        itemsList.fastForEach {
            DropdownMenuItem(text = { Text(text = it) }, onClick = {
                selectItem(it)
                onDismiss()

            })
        }


        DropdownMenuItem(
            text = {
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
            },
            onClick = {
                createCategory()
                onDismiss()

            })


    }
}

@Composable
fun createCategoryDialog(
    createCategoryUiState: CreateCategoryUiState,
    onValueChange: (category: String) -> Unit,
    onDismiss: () -> Unit,
    createCategory: () -> Unit
) {
    if (createCategoryUiState.showCreateCategoryDialog) {
        Dialog(
            onDismissRequest = onDismiss
        ) {

            createCategoryDialogContent(
                createCategoryUiState = createCategoryUiState,
                onValueChange = onValueChange,
                createCategory = createCategory
            )
        }
    }

}


@Composable
fun createCategoryDialogContent(
    createCategoryUiState: CreateCategoryUiState,
    onValueChange: (category: String) -> Unit,
    createCategory: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {

    Card(
        modifier = modifier
            .height(200.dp)
            .padding(10.dp),
        shape = RoundedCornerShape(16.dp),
    ){
        Column(modifier = modifier
            .padding(10.dp)) {
            OutlinedTextField(
                label = { Text(text = stringResource(id = R.string.category_name)) },
                modifier = modifier,
                value = createCategoryUiState.categoryName, onValueChange = onValueChange
            )
            if (createCategoryUiState.showErrorMessage) {
                Text(
                    style = TextStyle(color = Color.Red),
                    text = stringResource(id = R.string.introduce_category_name)
                )
            }
            ElevatedButton(
                modifier = Modifier
                    .align(alignment = Alignment.End)
                    .padding(0.dp, 10.dp),
                onClick = {
                    createCategory()
                }) {
                Text(text = stringResource(id = R.string.save))
            }
        }

    }


}


@Composable
fun showToast(message: Int){
    val ctx = LocalContext.current
    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
}



@Preview(showBackground = true)
@Composable
fun datePickerPreview() {
    dateModifier(
        datePickerToBeShown = false,
        selectedDateInMilliseconds = Utils.getCurrentTimeInMilliseconds(),
        selectedDay = 10,
        showDatePicker = { /*TODO*/ },
        hideDatePicker = { /*TODO*/ },
        setDate = {})
}


@Preview(showBackground = true)
@Composable
fun createCategoryDialogPreview() {
    createCategoryDialogContent(
        CreateCategoryUiState(),
        createCategory = {},
        onValueChange = {})
}

