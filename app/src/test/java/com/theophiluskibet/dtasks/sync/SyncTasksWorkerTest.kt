package com.theophiluskibet.dtasks.sync

import com.theophiluskibet.dtasks.data.local.entity.TaskEntity
import com.theophiluskibet.dtasks.domain.models.TaskModel
import com.theophiluskibet.dtasks.domain.repository.SyncRepository
import com.theophiluskibet.dtasks.helpers.LocalDateTime
import com.theophiluskibet.dtasks.helpers.asEpochMilliseconds
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SyncTasksWorkerTest {

    private lateinit var fakeSyncRepository: FakeSyncRepository

    @OptIn(ExperimentalTime::class)
    private val time = Clock.System.now().LocalDateTime

    @Before
    fun setUp() {
        fakeSyncRepository = FakeSyncRepository()
    }

    @Test
    fun syncLogic_successfulPushAndFetch_shouldCompleteAllSteps() = runTest {
        // Given
        fakeSyncRepository.pushTasksResult = Result.success(true)
        fakeSyncRepository.fetchTasksResult = Result.success(emptyList())

        // When - simulate the sync logic from doWork()
        val syncResult = performSyncLogic(fakeSyncRepository)

        // Then
        assertTrue(syncResult.isSuccess)
        assertTrue(fakeSyncRepository.pushTasksCalled)
        assertTrue(fakeSyncRepository.fetchTasksCalled)
        assertTrue(fakeSyncRepository.mergeTasksCalled)
        assertTrue(fakeSyncRepository.updateLastSyncTimeCalled)
    }

    @Test
    fun syncLogic_pushTasksFails_shouldReturnFailure() = runTest {
        // Given
        fakeSyncRepository.pushTasksResult = Result.failure(Exception("Push failed"))

        // When
        val syncResult = performSyncLogic(fakeSyncRepository)

        // Then
        assertTrue(syncResult.isFailure)
        assertTrue(fakeSyncRepository.pushTasksCalled)
        assertFalse(fakeSyncRepository.fetchTasksCalled) // Should not reach fetch
    }

    @Test
    fun syncLogic_fetchTasksFails_shouldReturnFailure() = runTest {
        // Given
        fakeSyncRepository.pushTasksResult = Result.success(true)
        fakeSyncRepository.fetchTasksResult = Result.failure(Exception("Fetch failed"))

        // When
        val syncResult = performSyncLogic(fakeSyncRepository)

        // Then
        assertTrue(syncResult.isFailure)
        assertTrue(fakeSyncRepository.pushTasksCalled)
        assertTrue(fakeSyncRepository.fetchTasksCalled)
        assertFalse(fakeSyncRepository.mergeTasksCalled) // Should not reach merge
    }

    @Test
    fun syncLogic_pushTasksReturnsTrue_shouldUpdateSyncedTasks() = runTest {
        // Given
        val unsyncedTasks = listOf(
            TaskEntity("1", "Task 1", "", null, false, time.asEpochMilliseconds(), time.asEpochMilliseconds()),
            TaskEntity("2", "Task 2", "", null, false, time.asEpochMilliseconds(), time.asEpochMilliseconds())
        )

        fakeSyncRepository.pushTasksResult = Result.success(true)
        fakeSyncRepository.fetchTasksResult = Result.success(emptyList())
        fakeSyncRepository.unsyncedTasks = unsyncedTasks

        // When
        val syncResult = performSyncLogic(fakeSyncRepository)

        // Then
        assertTrue(syncResult.isSuccess)
        assertTrue(fakeSyncRepository.updateSyncedTasksCalled)
        assertEquals(
            unsyncedTasks.map { it.id },
            fakeSyncRepository.lastUpdatedSyncedTaskIds
        )
    }

    @Test
    fun syncLogic_pushTasksReturnsFalse_shouldNotUpdateSyncedTasks() = runTest {
        // Given
        fakeSyncRepository.pushTasksResult = Result.success(false)
        fakeSyncRepository.fetchTasksResult = Result.success(emptyList())

        // When
        val syncResult = performSyncLogic(fakeSyncRepository)

        // Then
        assertTrue(syncResult.isSuccess)
        assertFalse(fakeSyncRepository.updateSyncedTasksCalled)
    }

    @Test
    fun syncLogic_withRemoteTasks_shouldMergeTasks() = runTest {
        // Given
        val remoteTasks = listOf(
            TaskModel("remote-1", "Remote Task 1", dueDate = null, createdAt = time, updatedAt = time),
            TaskModel("remote-2", "Remote Task 2", dueDate = null, createdAt =time, updatedAt = time)
        )

        fakeSyncRepository.pushTasksResult = Result.success(true)
        fakeSyncRepository.fetchTasksResult = Result.success(remoteTasks)

        // When
        val syncResult = performSyncLogic(fakeSyncRepository)

        // Then
        assertTrue(syncResult.isSuccess)
        assertTrue(fakeSyncRepository.mergeTasksCalled)
        assertEquals(remoteTasks, fakeSyncRepository.lastMergedTasks)
    }

    @Test
    fun syncLogic_exceptionThrown_shouldReturnFailure() = runTest {
        // Given
        fakeSyncRepository.pushTasksResult = Result.success(true)
        fakeSyncRepository.fetchTasksResult = Result.success(emptyList())
        fakeSyncRepository.shouldThrowException = true

        // When
        val syncResult = performSyncLogic(fakeSyncRepository)

        // Then
        assertTrue(syncResult.isFailure)
    }

    // Helper method that simulates the sync logic from SyncTasksWorker.doWork()
    private suspend fun performSyncLogic(repository: SyncRepository): Result<Unit> {
        return try {
            // Step 1: Push local changes to server
            val pushResult = repository.pushTasks()
            if (pushResult.isFailure) {
                return Result.failure(pushResult.exceptionOrNull() ?: Exception("Push failed"))
            }

            val syncResponse = pushResult.getOrNull()
            if (syncResponse == true) {
                // Mark pushed tasks as synced
                val pendingTasks = repository.getUnsyncedTasks()
                repository.updateSyncedTasks(pendingTasks.map { it.id })
            }

            // Step 2: Fetch remote changes
            val fetchResult = repository.fetchTasks()
            if (fetchResult.isFailure) {
                return Result.failure(fetchResult.exceptionOrNull() ?: Exception("Fetch failed"))
            }

            val remoteTasks = fetchResult.getOrNull() ?: emptyList()

            // Step 3: Merge remote changes using Last Write Wins
            repository.mergeTasks(remoteTasks)

            // Step 4: Update last sync time
            repository.updateLastSyncTime()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Fake SyncRepository for testing
class FakeSyncRepository : SyncRepository {
    var pushTasksCalled = false
    var fetchTasksCalled = false
    var mergeTasksCalled = false
    var updateLastSyncTimeCalled = false
    var updateSyncedTasksCalled = false
    var getUnsyncedTasksCalled = false

    var pushTasksResult: Result<Boolean> = Result.success(true)
    var fetchTasksResult: Result<List<TaskModel>> = Result.success(emptyList())
    var unsyncedTasks: List<TaskEntity> = emptyList()
    var shouldThrowException = false

    var lastMergedTasks: List<TaskModel>? = null
    var lastUpdatedSyncedTaskIds: List<String>? = null

    override suspend fun pushTasks(): Result<Boolean> {
        pushTasksCalled = true
        if (shouldThrowException) throw RuntimeException("Test exception")
        return pushTasksResult
    }

    override suspend fun fetchTasks(): Result<List<TaskModel>> {
        fetchTasksCalled = true
        if (shouldThrowException) throw RuntimeException("Test exception")
        return fetchTasksResult
    }

    override suspend fun mergeTasks(remoteTasks: List<TaskModel>) {
        mergeTasksCalled = true
        lastMergedTasks = remoteTasks
        if (shouldThrowException) throw RuntimeException("Test exception")
    }

    override suspend fun updateLastSyncTime() {
        updateLastSyncTimeCalled = true
        if (shouldThrowException) throw RuntimeException("Test exception")
    }

    override suspend fun getUnsyncedTasks(): List<TaskEntity> {
        getUnsyncedTasksCalled = true
        return unsyncedTasks
    }

    override suspend fun updateSyncedTasks(taskIds: List<String>) {
        updateSyncedTasksCalled = true
        lastUpdatedSyncedTaskIds = taskIds
        if (shouldThrowException) throw RuntimeException("Test exception")
    }
}