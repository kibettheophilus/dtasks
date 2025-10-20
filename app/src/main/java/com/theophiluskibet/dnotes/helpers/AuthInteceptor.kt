package com.theophiluskibet.dnotes.helpers

import com.theophiluskibet.dnotes.data.local.preferences.PreferenceManager
import okhttp3.Interceptor

class AuthInterceptor(private val preferenceManager: PreferenceManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        // todo: fix this
        val token = preferenceManager.fetchToken
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}
