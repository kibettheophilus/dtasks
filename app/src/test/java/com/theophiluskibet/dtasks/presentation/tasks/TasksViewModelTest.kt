package com.theophiluskibet.dtasks.presentation.tasks

import com.theophiluskibet.dtasks.domain.models.TaskModel
import com.theophiluskibet.dtasks.domain.repository.TasksRepository
import com.theophiluskibet.dtasks.helpers.LocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class TasksViewModelTest {

    private lateinit var fakeRepository: FakeTasksRepository

    @OptIn(ExperimentalTime::class)
    private val time = Clock.System.now().LocalDateTime
    private val sampleTask = TaskModel(
        id = "task-1",
        title = "Test Task",
        description = "Test Description",
        dueDate = time,
        isCompleted = false,
        createdAt = time,
        updatedAt = time
    )

    @Before
    fun setUp() {
        fakeRepository = FakeTasksRepository()
    }

    @Test
    fun editTask_shouldShowBottomSheetWithTask() = runTest {
        // Given
        val viewModel = TasksViewModel(fakeRepository)

        // When
        viewModel.editTask(sampleTask)

        // Then
        val uiState = viewModel.uiState.value
        assertTrue(uiState.showBottomSheet)
        assertEquals(sampleTask, uiState.taskToEdit)
    }

    @Test
    fun addTask_shouldShowBottomSheetWithoutTask() = runTest {
        // Given
        val viewModel = TasksViewModel(fakeRepository)

        // When
        viewModel.addTask()

        // Then
        val uiState = viewModel.uiState.value
        assertTrue(uiState.showBottomSheet)
        assertNull(uiState.taskToEdit)
    }

    @Test
    fun hideBottomSheet_shouldHideBottomSheetAndClearTaskToEdit() = runTest {
        // Given
        val viewModel = TasksViewModel(fakeRepository)
        viewModel.addTask() // Show bottom sheet first

        // When
        viewModel.hideBottomSheet()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.showBottomSheet)
        assertNull(uiState.taskToEdit)
    }

    @Test
    fun saveTask_shouldCallRepositoryInsert() = runTest {
        // Given
        val viewModel = TasksViewModel(fakeRepository)

        // When
        viewModel.saveTask(sampleTask)
        kotlinx.coroutines.delay(100) // Wait for async operation to complete

        // Then
        assertTrue(fakeRepository.insertTaskCalled)
        assertEquals(sampleTask, fakeRepository.lastInsertedTask)

        // Should also hide bottom sheet
        val uiState = viewModel.uiState.value
        assertFalse(uiState.showBottomSheet)
        assertNull(uiState.taskToEdit)
    }

    @Test
    fun toggleTaskCompletion_shouldCallRepositoryUpdate() = runTest {
        // Given
        val viewModel = TasksViewModel(fakeRepository)

        // When
        viewModel.toggleTaskCompletion(sampleTask)
        kotlinx.coroutines.delay(100) // Wait for async operation to complete

        // Then
        assertTrue(fakeRepository.updateTaskCalled)
        val updatedTask = fakeRepository.lastUpdatedTask
        assertNotNull(updatedTask)
        assertEquals(sampleTask.id, updatedTask?.id)
        assertTrue(updatedTask?.isCompleted ?: false) // Should be toggled to true
    }

    @Test
    fun deleteTask_shouldCallRepositoryDelete() = runTest {
        // Given
        val viewModel = TasksViewModel(fakeRepository)

        // When
        viewModel.deleteTask(sampleTask)
        kotlinx.coroutines.delay(100) // Wait for async operation to complete

        // Then
        assertTrue(fakeRepository.deleteTaskCalled)
        assertEquals(sampleTask.id, fakeRepository.lastDeletedTaskId)
    }

    @Test
    fun onTaskClick_shouldEditTask() = runTest {
        // Given
        val viewModel = TasksViewModel(fakeRepository)

        // When
        viewModel.onTaskClick(sampleTask)

        // Then
        val uiState = viewModel.uiState.value
        assertTrue(uiState.showBottomSheet)
        assertEquals(sampleTask, uiState.taskToEdit)
    }
}

// Fake repository implementation for testing
class FakeTasksRepository : TasksRepository {
    private val tasks = mutableListOf<TaskModel>()

    var insertTaskCalled = false
    var updateTaskCalled = false
    var deleteTaskCalled = false

    var lastInsertedTask: TaskModel? = null
    var lastUpdatedTask: TaskModel? = null
    var lastDeletedTaskId: String? = null

    override suspend fun getTasks(): Flow<List<TaskModel>> {
        return flowOf(tasks.toList())
    }

    override suspend fun insertTask(task: TaskModel) {
        insertTaskCalled = true
        lastInsertedTask = task
        tasks.add(task)
    }

    override suspend fun updateTask(task: TaskModel) {
        updateTaskCalled = true
        lastUpdatedTask = task

        val index = tasks.indexOfFirst { it.id == task.id }
        if (index >= 0) {
            tasks[index] = task
        }
    }

    override suspend fun deleteTask(id: String) {
        deleteTaskCalled = true
        lastDeletedTaskId = id
        tasks.removeAll { it.id == id }
    }
}