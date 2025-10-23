package com.theophiluskibet.dtasks.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.theophiluskibet.dtasks.data.local.database.TasksDatabase
import com.theophiluskibet.dtasks.data.local.entity.TaskEntity
import com.theophiluskibet.dtasks.helpers.LocalDateTime
import com.theophiluskibet.dtasks.helpers.asEpochMilliseconds
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

@RunWith(AndroidJUnit4::class)
class TasksDaoTest {

    private lateinit var database: TasksDatabase
    private lateinit var tasksDao: TasksDao

    @OptIn(ExperimentalTime::class)
    private val time = Clock.System.now().LocalDateTime.asEpochMilliseconds()

    private val sampleTask = TaskEntity(
        id = "task-1",
        title = "Test Task",
        description = "This is a test task",
        dueDate = time,
        isCompleted = false,
        createdAt = time,
        updatedAt = time
    )

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            TasksDatabase::class.java
        ).build()
        tasksDao = database.tasksDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertTask_shouldInsertTaskSuccessfully() = runTest {
        // When
        tasksDao.insertTask(sampleTask)

        // Then
        val tasks = tasksDao.getTasks().first()
        assertEquals(1, tasks.size)
        assertEquals(sampleTask.id, tasks[0].id)
        assertEquals(sampleTask.title, tasks[0].title)
    }

    @Test
    fun insertTask_withConflict_shouldReplaceTask() = runTest {
        // Given
        tasksDao.insertTask(sampleTask)
        val updatedTask = sampleTask.copy(title = "Updated Task")

        // When
        tasksDao.insertTask(updatedTask)

        // Then
        val tasks = tasksDao.getTasks().first()
        assertEquals(1, tasks.size)
        assertEquals("Updated Task", tasks[0].title)
    }

    @Test
    fun updateTask_shouldUpdateTaskSuccessfully() = runTest {
        // Given
        tasksDao.insertTask(sampleTask)
        val updatedTask = sampleTask.copy(
            title = "Updated Task",
            isCompleted = true
        )

        // When
        tasksDao.updateTask(updatedTask)

        // Then
        val task = tasksDao.getTaskById(sampleTask.id)
        assertNotNull(task)
        assertEquals("Updated Task", task?.title)
        assertTrue(task?.isCompleted ?: false)
    }

    @Test
    fun getTaskById_existingTask_shouldReturnTask() = runTest {
        // Given
        tasksDao.insertTask(sampleTask)

        // When
        val task = tasksDao.getTaskById(sampleTask.id)

        // Then
        assertNotNull(task)
        assertEquals(sampleTask.id, task?.id)
        assertEquals(sampleTask.title, task?.title)
    }

    @Test
    fun getTaskById_nonExistingTask_shouldReturnNull() = runTest {
        // When
        val task = tasksDao.getTaskById("non-existing-id")

        // Then
        assertNull(task)
    }

    @Test
    fun getTasks_emptyDatabase_shouldReturnEmptyList() = runTest {
        // When
        val tasks = tasksDao.getTasks().first()

        // Then
        assertTrue(tasks.isEmpty())
    }

    @Test
    fun getTasks_withMultipleTasks_shouldReturnAllTasks() = runTest {
        // Given
        val task1 = sampleTask
        val task2 = sampleTask.copy(id = "task-2", title = "Second Task")
        val task3 = sampleTask.copy(id = "task-3", title = "Third Task")

        tasksDao.insertTask(task1)
        tasksDao.insertTask(task2)
        tasksDao.insertTask(task3)

        // When
        val tasks = tasksDao.getTasks().first()

        // Then
        assertEquals(3, tasks.size)
        val taskIds = tasks.map { it.id }
        assertTrue(taskIds.contains("task-1"))
        assertTrue(taskIds.contains("task-2"))
        assertTrue(taskIds.contains("task-3"))
    }

    @Test
    fun deleteTaskById_existingTask_shouldDeleteTask() = runTest {
        // Given
        tasksDao.insertTask(sampleTask)
        assertEquals(1, tasksDao.getTasks().first().size)

        // When
        tasksDao.deleteTaskById(sampleTask.id)

        // Then
        val tasks = tasksDao.getTasks().first()
        assertTrue(tasks.isEmpty())
    }

    @Test
    fun deleteTaskById_nonExistingTask_shouldNotCrash() = runTest {
        // Given
        tasksDao.insertTask(sampleTask)

        // When - no exception should be thrown
        tasksDao.deleteTaskById("non-existing-id")

        // Then
        val tasks = tasksDao.getTasks().first()
        assertEquals(1, tasks.size) // Original task should still be there
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun getTasksBySyncTime_shouldReturnTasksAfterGivenTime() = runTest {
        // Given
        val oldTask = sampleTask.copy(
            id = "old-task",
            updatedAt = time // Old timestamp
        )
        val newTask = sampleTask.copy(
            id = "new-task",
            updatedAt = Clock.System.now()
                .plus(1.hours).LocalDateTime.asEpochMilliseconds() // New timestamp
        )

        tasksDao.insertTask(oldTask)
        tasksDao.insertTask(newTask)

        // When
        val tasks = tasksDao.getTasksBySyncTime(1500)

        // Then
        assertEquals(1, tasks.size)
        assertEquals("new-task", tasks[0].id)
    }

    @Test
    fun getTasksBySyncTime_noTasksAfterTime_shouldReturnEmptyList() = runTest {
        // Given
        val oldTask = sampleTask.copy(updatedAt = time)
        tasksDao.insertTask(oldTask)

        // When
        val tasks = tasksDao.getTasksBySyncTime(2000)

        // Then
        assertTrue(tasks.isEmpty())
    }
}