package com.theophiluskibet.dtasks.sync

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.theophiluskibet.dtasks.domain.repository.SyncRepository
import java.util.concurrent.TimeUnit

/**
 * Constraints for the sync worker, requiring network, battery, and storage.
 */
private val constraints =
    Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .setRequiresStorageNotLow(true)
        .build()

/**
 * Schedules a periodic sync of tasks.
 */
fun Context.schedulePeriodicSync() {
    val syncWorkRequest = PeriodicWorkRequestBuilder<SyncTasksWorker>(
        15, TimeUnit.MINUTES
    )
        .setConstraints(constraints)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            WorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS
        )
        .build()

    WorkManager.getInstance(this).enqueueUniquePeriodicWork(
        "task_sync",
        ExistingPeriodicWorkPolicy.KEEP,
        syncWorkRequest
    )
}

/**
 * Triggers an immediate sync of tasks.
 */
fun Context.triggerImmediateSync() {
    val syncWorkRequest = OneTimeWorkRequestBuilder<SyncTasksWorker>()
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(this).enqueueUniqueWork(
        "immediate_sync",
        ExistingWorkPolicy.REPLACE,
        syncWorkRequest
    )
}

/**
 * A [CoroutineWorker] that syncs tasks between the local database and the remote server.
 *
 * @param context The application context.
 * @param params The worker parameters.
 * @param syncRepository The repository for handling syncing.
 */
class SyncTasksWorker(
    context: Context,
    params: WorkerParameters,
    private val syncRepository: SyncRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Step 1: Push local changes to server
            val pushResult = syncRepository.pushTasks()

            if (pushResult.isFailure) {
                return Result.retry()
            }

            val syncResponse = pushResult.getOrNull()
            if (syncResponse == true) {
                // Mark pushed tasks as synced
                val pendingTasks = syncRepository.getUnsyncedTasks()

                syncRepository.updateSyncedTasks(pendingTasks.map { it.id })
            }

            // Step 2: Fetch remote changes
            val fetchResult = syncRepository.fetchTasks()

            if (fetchResult.isFailure) {
                return Result.retry()
            }

            val remoteTasks = fetchResult.getOrNull() ?: emptyList()

            // Step 3: Merge remote changes using Last Write Wins
            syncRepository.mergeTasks(remoteTasks)

            // Step 4: Update last sync time
            syncRepository.updateLastSyncTime()

            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
}
