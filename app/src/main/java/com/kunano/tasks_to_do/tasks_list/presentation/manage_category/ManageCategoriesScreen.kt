package com.kunano.tasks_to_do.tasks_list.presentation.manage_category

import Route
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kunano.tasks_to_do.R
import com.kunano.tasks_to_do.core.utils.basicAlertDialog
import com.kunano.tasks_to_do.core.utils.createCategoryDialog
import com.kunano.tasks_to_do.core.utils.navigateBackButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCategoriesScreen(
    paddingValues: PaddingValues,
    navigate: (route: Route) -> Unit,
    navigateBack: () -> Unit,
    viewModel: ManageCategoriesViewModel = hiltViewModel()
) {


    val manageCategoriesScreenState by viewModel.manageCategoriesScreenState.collectAsStateWithLifecycle()


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var list by remember { mutableStateOf(mutableListOf("")) }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = { navigateBackButton(navigateBack = navigateBack) },
                actions = {
                    IconButton(onClick = viewModel::addCategory) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                },
                title = { Text(text = stringResource(id = R.string.manage_categories)) })

            categoryList(
                categoriesList = list, paddingValues,
                edit = viewModel::edit,
                delete = viewModel::requestCategoryDeleting,
            )
        }

        if (manageCategoriesScreenState.showDeletingAlertDialog){
            basicAlertDialog(confirmButtonText = R.string.confirm,
                dismissButtonText = R.string.cancel,
                title = R.string.delete,
                body = R.string.delete_this_category,
                confirm = viewModel::confirmCategoryDeleting,
                dismiss = viewModel::hideDeletingAlertDialog)
        }


        createCategoryDialog(
            createCategoryUiState = manageCategoriesScreenState,
            onValueChange = viewModel::onChangeCategoryName,
            buttonTitle = if (manageCategoriesScreenState.editMode) R.string.update else R.string.save,
            onDismiss = { viewModel.hideCreateOrUpdateTaskDialog() },
            createCategory = viewModel::saveChanges)

    }


}


@Composable
fun categoryList(
    categoriesList: List<String>,
    paddingValues: PaddingValues,
    edit: (categoryId: String) -> Unit,
    delete: (categoryId: String) -> Unit,
) {

    val columState = rememberLazyListState();
    LazyColumn(
        state = columState,
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp, 0.dp),
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(20) { categoryName ->
            categoryCard(
                categoryName = "categoryName $categoryName",
                edit = edit,
                delete = delete
            )
        }
    }
}

@Composable
fun categoryCard(
    categoryName: String,
    edit: (categoryId: String) -> Unit,
    delete: (categoryId: String) -> Unit,
    modifier: Modifier = Modifier
) {

    var isDropDownMenuExpanded by remember {
        mutableStateOf(false)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {

        Text(
            modifier = modifier
                .padding(10.dp, 20.dp)
                .weight(1f), text = categoryName
        )
        Text(modifier = modifier.padding(10.dp, 0.dp), text = "0")

        IconButton(onClick = { isDropDownMenuExpanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = null)
        }

        categoryDropDownMenu(
            isExpanded = isDropDownMenuExpanded,
            categoryId = categoryName,
            edit = edit,
            delete = delete,
            onDismiss = { isDropDownMenuExpanded = false },
        )
    }
}


@Composable
fun categoryDropDownMenu(
    isExpanded: Boolean,
    categoryId: String,
    edit: (categoryId: String) -> Unit,
    delete: (categoryId: String) -> Unit,
    onDismiss: () -> Unit
) {

    DropdownMenu(
        offset = DpOffset(x = (-16).dp, y = 0.dp),
        expanded = isExpanded, onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.edit)) },
            onClick = {
                onDismiss()
                edit(categoryId)
            })

        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.delete)) },
            onClick = {
                onDismiss()
                delete(categoryId)
            })
    }
}


@Preview(showBackground = true)
@Composable
fun categoryCardPreview() {
    categoryCard(
        categoryName = "Example",
        edit = { },
        delete = { },
    )
}


@Preview(showBackground = true)
@Composable
fun categoryListPreview() {
    categoryList(listOf(), PaddingValues(),
        edit = { },
        delete = { })
}
