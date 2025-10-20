package com.theophiluskibet.dnotes.data.remote.api

import com.theophiluskibet.dnotes.data.remote.models.TaskDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TasksApi {
    @GET("/tasks")
    suspend fun getTasks(@Query("since") since: Long): Response<List<TaskDto>>

    @POST("/sync")
    suspend fun syncTasks(@Body tasks: List<TaskDto>): Response<List<TaskDto>>
}