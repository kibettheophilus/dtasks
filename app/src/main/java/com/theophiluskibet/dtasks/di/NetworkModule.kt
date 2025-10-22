package com.theophiluskibet.dtasks.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.theophiluskibet.dtasks.data.local.preferences.PreferenceManager
import com.theophiluskibet.dtasks.data.remote.api.AuthApi
import com.theophiluskibet.dtasks.data.remote.api.TasksApi
import com.theophiluskibet.dtasks.helpers.AuthInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


val networkModule = module {
    singleOf(::provideOkHttpClient)
    singleOf(::provideRetrofit)
    singleOf(::provideJson)
    singleOf(::provideTasksApi)
    singleOf(::provideAuthApi)
    singleOf(::PreferenceManager)
    singleOf(::AuthInterceptor)
}

private fun provideOkHttpClient(preferenceManager: PreferenceManager): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(preferenceManager = preferenceManager))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {

    return Retrofit.Builder()
        .baseUrl("https://5a7a0a086f03.ngrok-free.app/")
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}

private fun provideJson(): Json {
    return Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }
}

private fun provideTasksApi(retrofit: Retrofit): TasksApi {
    return retrofit.create(TasksApi::class.java)
}

private fun provideAuthApi(retrofit: Retrofit): AuthApi {
    return retrofit.create(AuthApi::class.java)
}