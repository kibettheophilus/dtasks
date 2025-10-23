package com.theophiluskibet.dtasks.helpers

import com.theophiluskibet.dtasks.data.local.preferences.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * An [Interceptor] that adds an authorization header to requests.
 *
 * @param preferenceManager The [PreferenceManager] for fetching the authentication token.
 */
class AuthInterceptor(private val preferenceManager: PreferenceManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = preferenceManager.fetchToken
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}
