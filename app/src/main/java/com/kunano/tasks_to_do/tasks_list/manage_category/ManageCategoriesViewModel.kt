package com.kunano.tasks_to_do.tasks_list.manage_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunano.tasks_to_do.core.data.CategoryRepository
import com.kunano.tasks_to_do.core.data.model.entities.LocalCategoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ManageCategoriesScreenState(
    val showDeletingAlertDialog: Boolean = false,
    val editMode: Boolean = false,
    val categoryList: List<LocalCategoryEntity> = listOf(),
    val showCreateCategoryDialog: Boolean = false,
    val categoryName: String = "",
    val showErrorMessage: Boolean = false
)


@HiltViewModel
class ManageCategoriesViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    ViewModel() {

    private val _manageCategoriesScreenState: MutableStateFlow<ManageCategoriesScreenState> =
        MutableStateFlow(ManageCategoriesScreenState())


    val manageCategoriesScreenState: StateFlow<ManageCategoriesScreenState> =
        _manageCategoriesScreenState.asStateFlow()


    private var categoryToUpdateOrDelete: LocalCategoryEntity? = null


    var newCategoryReceiver: ((category: LocalCategoryEntity) -> Unit)? = null

    init {
        viewModelScope.launch {
            categoryRepository.getAllLive().collect {
                updateCategoryList(it)
            }
        }

    }


    fun showAddCategoryDialog() {
        deactivateEditingMode()
        showCreateOrUpdateTaskDialog()
    }


    fun requestCategoryEditing(category: LocalCategoryEntity) {
        categoryToUpdateOrDelete = category
        activateEditingMode()
        setCategoryName(name = category.categoryName)
        showCreateOrUpdateTaskDialog()
    }


    fun saveChanges() {
        val categoryName: String = _manageCategoriesScreenState.value.categoryName
        if (categoryName.isEmpty()) {
            showErrorMessage()
            return
        }

        if (_manageCategoriesScreenState.value.editMode) {
            updateCategory(categoryName)
        } else {
            createCategory(categoryName)
        }
        hideCreateOrUpdateTaskDialog()
        hideErrorMessage()
    }

    private fun updateCategory(categoryName: String) {
        viewModelScope.launch {
            categoryToUpdateOrDelete?.let {
                it.categoryName = categoryName
                categoryRepository.updateCategory(it)
            }
        }
    }

    private fun createCategory(categoryName: String) {
        val newCategory = LocalCategoryEntity(categoryName = categoryName)
        //Create
        viewModelScope.launch {
            val category = categoryRepository.insertAndReturnCategory(newCategory)
            category?.let { category ->
                newCategoryReceiver?.let {
                    it(category)
                }
            }
        }

    }


    fun requestCategoryDeleting(category: LocalCategoryEntity) {
        categoryToUpdateOrDelete = category
        showDeletingAlertDialog()
    }

    fun confirmCategoryDeleting() {
        categoryToUpdateOrDelete?.let {
            viewModelScope.launch {
                categoryRepository.deleteCategory(it)
            }
        }
        hideDeletingAlertDialog()
    }


    private fun showDeletingAlertDialog() {
        updateShowDeletingAlertDialog(true)
    }

    fun hideDeletingAlertDialog() {
        updateShowDeletingAlertDialog(false)
    }

    private fun hideErrorMessage() {
        updateShowErrorMessageState(show = false)
    }

    private fun showErrorMessage() {
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


    private fun updateCategoryList(categoryList: List<LocalCategoryEntity>) {
        _manageCategoriesScreenState.update { currentValue ->
            currentValue.copy(categoryList = categoryList)
        }
    }

    private fun updateShowErrorMessageState(show: Boolean) {
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