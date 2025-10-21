package com.theophiluskibet.dtasks.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theophiluskibet.dtasks.domain.models.TaskModel
import com.theophiluskibet.dtasks.domain.repository.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class TasksViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    init {
        getTasks()
    }

    private fun getTasks() {
        viewModelScope.launch {
            tasksRepository.getTasks().collect { tasks ->
                _uiState.update {
                    it.copy(tasks = tasks)
                }
            }
        }
    }

    fun toggleTaskCompletion(task: TaskModel) {
        viewModelScope.launch {
            tasksRepository.updateTask(
                task = task.copy(
                    isCompleted = !task.isCompleted,
                    updatedAt = Date().toString()
                )
            )
        }
    }

    fun deleteTask(task: TaskModel) {
        viewModelScope.launch {
            tasksRepository.deleteTask(id = task.id)
        }
    }

    fun editTask(task: TaskModel) {
        _uiState.update {
            it.copy(
                showBottomSheet = true,
                taskToEdit = task
            )
        }
    }

    fun addTask() {
        _uiState.update {
            it.copy(
                showBottomSheet = true,
                taskToEdit = null
            )
        }
    }

    fun saveTask(task: TaskModel) {
        viewModelScope.launch {
            tasksRepository.insertTask(task = task)
            hideBottomSheet()
        }
    }

    fun hideBottomSheet() {
        _uiState.update {
            it.copy(
                showBottomSheet = false,
                taskToEdit = null
            )
        }
    }

    fun onTaskClick(task: TaskModel) {
        // Navigate to task details or edit
        editTask(task)
    }
}

data class TasksUiState(
    val tasks: List<TaskModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showBottomSheet: Boolean = false,
    val taskToEdit: TaskModel? = null
)