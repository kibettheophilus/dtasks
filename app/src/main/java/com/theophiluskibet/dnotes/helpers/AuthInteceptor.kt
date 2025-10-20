package com.theophiluskibet.dnotes.helpers

import com.theophiluskibet.dnotes.data.local.preferences.TokenProvider
import okhttp3.Interceptor

class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        // todo: fix this
        val token = tokenProvider.fetchToken
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}
