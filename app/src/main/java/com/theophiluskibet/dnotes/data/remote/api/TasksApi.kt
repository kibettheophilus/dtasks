package com.theophiluskibet.dnotes.data.remote.api

import com.theophiluskibet.dnotes.data.remote.models.TaskDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface TasksApi {
    @GET("/tasks")
    suspend fun getTasks(): Response<List<TaskDto>>

    @POST("/tasks")
    suspend fun createTasks(@Body tasks: List<TaskDto>): Response<List<TaskDto>>

    @PUT("/tasks/{id}")
    suspend fun updateTask(@Body task: TaskDto): Response<TaskDto>

    @DELETE("/tasks/{id}")
    suspend fun deleteTask(taskId: String): Response<Any>
}