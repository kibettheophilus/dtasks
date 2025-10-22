package com.theophiluskibet.dtasks.data.repository

import com.theophiluskibet.dtasks.data.local.dao.TasksDao
import com.theophiluskibet.dtasks.data.local.entity.TaskEntity
import com.theophiluskibet.dtasks.domain.models.TaskModel
import com.theophiluskibet.dtasks.helpers.LocalDateTime
import com.theophiluskibet.dtasks.helpers.asEpochMilliseconds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class TasksRepositoryImplTest {

    private lateinit var fakeTasksDao: FakeTasksDao
    private lateinit var repository: TasksRepositoryImpl

    @OptIn(ExperimentalTime::class)
    private val time = Clock.System.now().LocalDateTime
    private val sampleTaskModel = TaskModel(
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
        fakeTasksDao = FakeTasksDao()
        repository = TasksRepositoryImpl(fakeTasksDao)
    }

    @Test
    fun getTasks_shouldReturnFlowOfTaskModels() = runTest {
        // Given
        val taskEntity = TaskEntity(
            id = "task-1",
            title = "Test Task",
            description = "Test Description",
            dueDate = time.asEpochMilliseconds(),
            isCompleted = false,
            createdAt = time.asEpochMilliseconds(),
            updatedAt = time.asEpochMilliseconds()
        )
        fakeTasksDao.insertTask(taskEntity)

        // When
        val result = repository.getTasks()?.first()

        // Then
        assertEquals(1, result?.size)
        assertEquals(taskEntity.id, result?.get(0)?.id)
        assertEquals(taskEntity.title, result?.get(0)?.title)
        assertEquals(taskEntity.description, result?.get(0)?.description)
        assertEquals(taskEntity.isCompleted, result?.get(0)?.isCompleted)
    }

    @Test
    fun getTasks_emptyList_shouldReturnEmptyFlow() = runTest {
        // When
        val result = repository.getTasks()?.first()

        // Then
        assertTrue(result?.isEmpty() == true)
    }

    @Test
    fun insertTask_shouldStoreTaskCorrectly() = runTest {
        // When
        repository.insertTask(sampleTaskModel)

        // Then
        val tasks = repository.getTasks()?.first()
        assertEquals(1, tasks?.size)
        assertEquals(sampleTaskModel.id, tasks?.get(0)?.id)
        assertEquals(sampleTaskModel.title, tasks?.get(0)?.title)
    }

    @Test
    fun updateTask_shouldUpdateExistingTask() = runTest {
        // Given
        repository.insertTask(sampleTaskModel)
        val updatedTask = sampleTaskModel.copy(
            title = "Updated Task",
            isCompleted = true
        )

        // When
        repository.updateTask(updatedTask)

        // Then
        val task = fakeTasksDao.getTaskById(sampleTaskModel.id)
        assertNotNull(task)
        assertEquals("Updated Task", task?.title)
        assertTrue(task?.isCompleted ?: false)
    }

    @Test
    fun deleteTask_shouldRemoveTask() = runTest {
        // Given
        repository.insertTask(sampleTaskModel)
        assertEquals(1, repository.getTasks()?.first()?.size)

        // When
        repository.deleteTask(sampleTaskModel.id)

        // Then
        val tasks = repository.getTasks()?.first()
        assertTrue(tasks?.isEmpty() == true)
    }

    @Test
    fun deleteTask_nonExistingTask_shouldNotCrash() = runTest {
        // Given
        repository.insertTask(sampleTaskModel)

        // When - should not throw exception
        repository.deleteTask("non-existing-id")

        // Then
        val tasks = repository.getTasks()?.first()
        assertEquals(1, tasks?.size) // Original task should still be there
    }
}

// Fake implementation of TasksDao for testing
class FakeTasksDao : TasksDao {
    private val tasks = mutableMapOf<String, TaskEntity>()

    override fun insertTask(task: TaskEntity) {
        tasks[task.id] = task
    }

    override fun updateTask(task: TaskEntity) {
        if (tasks.containsKey(task.id)) {
            tasks[task.id] = task
        }
    }

    override fun getTaskById(id: String): TaskEntity? {
        return tasks[id]
    }

    override fun getTasks(): Flow<List<TaskEntity>> {
        return flowOf(tasks.values.toList())
    }

    override fun deleteTaskById(id: String) {
        tasks.remove(id)
    }

    override suspend fun getTasksBySyncTime(lastSyncTime: Long): List<TaskEntity> {
        return tasks.values.filter {
            it.updatedAt > lastSyncTime
        }
    }
}