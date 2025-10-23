package com.theophiluskibet.dtasks.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.theophiluskibet.dtasks.BuildConfig
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

/**
 * Koin module for providing networking components.
 */
val networkModule = module {
    /**
     * Provides a singleton instance of [OkHttpClient].
     */
    single { provideOkHttpClient(get()) }

    /**
     * Provides a singleton instance of [Retrofit].
     */
    single { provideRetrofit(get(), get()) }

    /**
     * Provides a singleton instance of [Json].
     */
    single { provideJson() }

    /**
     * Provides a singleton instance of [TasksApi].
     */
    single { provideTasksApi(get()) }

    /**
     * Provides a singleton instance of [AuthApi].
     */
    single { provideAuthApi(get()) }

    /**
     * Provides a singleton instance of [AuthInterceptor].
     */
    singleOf(::AuthInterceptor)
}

/**
 * Creates a new instance of [OkHttpClient].
 *
 * @param preferenceManager The [PreferenceManager] for accessing the authentication token.
 * @return A new instance of [OkHttpClient].
 */
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

/**
 * Creates a new instance of [Retrofit].
 *
 * @param okHttpClient The [OkHttpClient] to use for requests.
 * @param json The [Json] instance for serialization.
 * @return A new instance of [Retrofit].
 */
private fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}

/**
 * Creates a new instance of [Json].
 *
 * @return A new instance of [Json].
 */
private fun provideJson(): Json {
    return Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }
}

/**
 * Creates a new instance of [TasksApi].
 *
 * @param retrofit The [Retrofit] instance to use.
 * @return A new instance of [TasksApi].
 */
private fun provideTasksApi(retrofit: Retrofit): TasksApi {
    return retrofit.create(TasksApi::class.java)
}

/**
 * Creates a new instance of [AuthApi].
 *
 * @param retrofit The [Retrofit] instance to use.
 * @return A new instance of [AuthApi].
 */
private fun provideAuthApi(retrofit: Retrofit): AuthApi {
    return retrofit.create(AuthApi::class.java)
}
