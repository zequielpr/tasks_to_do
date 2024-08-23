package com.kunano.tasks_to_do.tasks_list.presentation.manage_category

import androidx.lifecycle.ViewModel
import com.kunano.tasks_to_do.tasks_list.presentation.create_task.CreateCategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class ManageCategoriesScreenState(
    val showDeletingAlertDialog: Boolean = false,
    val editMode: Boolean = false
)


@HiltViewModel
class ManageCategoriesViewModel @Inject constructor() : ViewModel() {
    private val _createCategoryUiState: MutableStateFlow<CreateCategoryUiState> =
        MutableStateFlow(CreateCategoryUiState())
    private val _manageCategoriesScreenState: MutableStateFlow<ManageCategoriesScreenState> =
        MutableStateFlow(ManageCategoriesScreenState())

    val createCategoryUiState: StateFlow<CreateCategoryUiState> =
        _createCategoryUiState.asStateFlow()


    val manageCategoriesScreenState: StateFlow<ManageCategoriesScreenState> =
        _manageCategoriesScreenState.asStateFlow()


    fun addCategory() {
        deactivateEditingMode()
        showCreateOrUpdateTaskDialog()
    }


    fun edit(categoryId: String) {
        activateEditingMode()
        setCategoryName(name = categoryId)
        showCreateOrUpdateTaskDialog()
    }

    fun requestCategoryDeleting(categoryId: String){
        showDeletingAlertDialog()
    }

    fun confirmCategoryDeleting(){
        hideDeletingAlertDialog()
    }





    private fun showDeletingAlertDialog() {
        updateShowDeletingAlertDialog(true)
    }

    fun hideDeletingAlertDialog() {
        updateShowDeletingAlertDialog(false)
    }



    private fun activateEditingMode() {
        updateMode(isEditingModeActive = true)
    }

    private fun deactivateEditingMode() {
        updateMode(isEditingModeActive = false)
    }


    fun showCreateOrUpdateTaskDialog() {
        updateShowCreateOrUpdateDialog(true)
    }

    fun hideCreateOrUpdateTaskDialog() {
        setCategoryName(name = "")
        updateShowCreateOrUpdateDialog(false)
    }

    fun onChangeCategoryName(name: String) {
        println("category: $name")
        setCategoryName(name)
    }



    private fun updateShowDeletingAlertDialog(show: Boolean) {
        _manageCategoriesScreenState.update { currentState ->
            currentState.copy(showDeletingAlertDialog = show)
        }
    }


    private fun updateMode(isEditingModeActive: Boolean) {
        _manageCategoriesScreenState.update { currentState ->
            currentState.copy(editMode = isEditingModeActive)
        }
    }


    private fun updateShowCreateOrUpdateDialog(show: Boolean) {
        _createCategoryUiState.update { currentState ->
            currentState.copy(showCreateCategoryDialog = show)
        }
    }

    private fun setCategoryName(name: String) {

        _createCategoryUiState.update { currentState ->
            currentState.copy(categoryName = name)
        }
    }
}