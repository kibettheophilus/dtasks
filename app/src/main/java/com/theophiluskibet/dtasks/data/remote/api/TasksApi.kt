package com.theophiluskibet.dtasks.data.remote.api

import com.theophiluskibet.dtasks.data.remote.models.TaskDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * API interface for task-related network requests.
 */
interface TasksApi {
    /**
     * Fetches tasks from the server.
     *
     * @param since The timestamp (in milliseconds) to fetch tasks since.
     * @return A [Response] containing a list of [TaskDto]s.
     */
    @GET("/tasks")
    suspend fun getTasks(@Query("since") since: Long): Response<List<TaskDto>>

    /**
     * Syncs a list of tasks with the server.
     *
     * @param tasks The list of tasks to sync.
     * @return A [Response] containing a list of [TaskDto]s.
     */
    @POST("/sync")
    suspend fun syncTasks(@Body tasks: List<TaskDto>): Response<List<TaskDto>>
}
