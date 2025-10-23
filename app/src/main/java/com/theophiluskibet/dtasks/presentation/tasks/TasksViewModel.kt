package com.theophiluskibet.dtasks.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theophiluskibet.dtasks.domain.models.TaskModel
import com.theophiluskibet.dtasks.domain.repository.TasksRepository
import com.theophiluskibet.dtasks.helpers.LocalDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * ViewModel for the Tasks screen.
 *
 * @param tasksRepository The repository for accessing task data.
 */
class TasksViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    init {
        getTasks()
    }

    /**
     * Fetches the list of tasks from the repository and updates the UI state.
     */
    private fun getTasks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                tasksRepository.getTasks()?.collect { tasks ->
                    _uiState.update {
                        it.copy(
                            tasks = tasks,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load tasks: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Toggles the completion status of a task.
     *
     * @param task The task to update.
     */
    @OptIn(ExperimentalTime::class)
    fun toggleTaskCompletion(task: TaskModel) {
        viewModelScope.launch {
            tasksRepository.updateTask(
                task = task.copy(
                    isCompleted = !task.isCompleted,
                    updatedAt = Clock.System.now().LocalDateTime
                )
            )
        }
    }

    /**
     * Deletes a task.
     *
     * @param task The task to delete.
     */
    fun deleteTask(task: TaskModel) {
        viewModelScope.launch {
            tasksRepository.deleteTask(id = task.id)
        }
    }

    /**
     * Shows the bottom sheet to edit a task.
     *
     * @param task The task to edit.
     */
    fun editTask(task: TaskModel) {
        _uiState.update {
            it.copy(
                showBottomSheet = true,
                taskToEdit = task
            )
        }
    }

    /**
     * Shows the bottom sheet to add a new task.
     */
    fun addTask() {
        _uiState.update {
            it.copy(
                showBottomSheet = true,
                taskToEdit = null
            )
        }
    }

    /**
     * Saves a new or updated task.
     *
     * @param task The task to save.
     */
    fun saveTask(task: TaskModel) {
        viewModelScope.launch {
            tasksRepository.insertTask(task = task)
            hideBottomSheet()
        }
    }

    /**
     * Hides the bottom sheet.
     */
    fun hideBottomSheet() {
        _uiState.update {
            it.copy(
                showBottomSheet = false,
                taskToEdit = null
            )
        }
    }

    /**
     * Handles the click event on a task.
     *
     * @param task The clicked task.
     */
    fun onTaskClick(task: TaskModel) {
        // Navigate to task details or edit
        editTask(task)
    }

    /**
     * Retries loading the tasks if there was an error.
     */
    fun retryLoadingTasks() {
        getTasks()
    }
}

/**
 * Represents the UI state for the Tasks screen.
 *
 * @param tasks The list of tasks to display.
 * @param isLoading Whether the tasks are currently being loaded.
 * @param errorMessage An error message to display if loading fails.
 * @param showBottomSheet Whether the bottom sheet for adding/editing a task is visible.
 * @param taskToEdit The task that is currently being edited.
 */
data class TasksUiState(
    val tasks: List<TaskModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showBottomSheet: Boolean = false,
    val taskToEdit: TaskModel? = null
)
