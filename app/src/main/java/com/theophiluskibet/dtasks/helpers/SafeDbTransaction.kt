package com.theophiluskibet.dtasks.helpers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A helper function that safely performs a database transaction.
 *
 * @param scope The [CoroutineDispatcher] to use for the transaction.
 * @param block The block of code to execute within the transaction.
 * @return A [Result] containing the result of the transaction.
 */
suspend fun <T> safeDbTransaction(
    scope: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> T,
): Result<T> {
    return withContext(scope) {
        try {
            val data = block()
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
