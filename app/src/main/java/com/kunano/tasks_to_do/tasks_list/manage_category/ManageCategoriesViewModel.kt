package com.kunano.tasks_to_do.tasks_list.manage_category

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class ManageCategoriesScreenState(
    val showDeletingAlertDialog: Boolean = false,
    val editMode: Boolean = false,
    val categoryList: List<String> = listOf(),
    val showCreateCategoryDialog: Boolean = false,
    val categoryName: String = "",
    val showErrorMessage: Boolean = false
)


@HiltViewModel
class ManageCategoriesViewModel @Inject constructor() : ViewModel() {

    private val _manageCategoriesScreenState: MutableStateFlow<ManageCategoriesScreenState> =
        MutableStateFlow(ManageCategoriesScreenState())


    val manageCategoriesScreenState: StateFlow<ManageCategoriesScreenState> =
        _manageCategoriesScreenState.asStateFlow()


    private var categoriesList = listOf(
        "category 1",
        "Task 2",
        "category 3",
    )

    init {
        _manageCategoriesScreenState.update { currentState ->
            currentState.copy(categoryList = categoriesList)
        }
    }




    fun showAddAcategoryDialog() {
        deactivateEditingMode()
        showCreateOrUpdateTaskDialog()
    }


    fun edit(categoryId: String) {
        activateEditingMode()
        setCategoryName(name = categoryId)
        showCreateOrUpdateTaskDialog()
    }


    fun saveChanges(){
        if (_manageCategoriesScreenState.value.categoryName.isEmpty()){
            showErrorMessage()
            return
        }

        if (_manageCategoriesScreenState.value.editMode){
            //Update
        }else{
            //Create

        }

        hideErrorMessage()
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

    private fun hideErrorMessage(){
        updateShowErrorMessageState(show = false)
    }
    private fun showErrorMessage(){
        updateShowErrorMessageState(show = true)
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
        hideErrorMessage()
        setCategoryName(name = "")
        updateShowCreateOrUpdateDialog(false)
    }

    fun onChangeCategoryName(name: String) {
        println("category: $name")
        setCategoryName(name)
    }


    private fun updateShowErrorMessageState(show: Boolean){
        _manageCategoriesScreenState.update { currentState ->
            currentState.copy(showErrorMessage = show)
        }
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
        _manageCategoriesScreenState.update { currentState ->
            currentState.copy(showCreateCategoryDialog = show)
        }
    }

    private fun setCategoryName(name: String) {

        _manageCategoriesScreenState.update { currentState ->
            currentState.copy(categoryName = name)
        }
    }


}