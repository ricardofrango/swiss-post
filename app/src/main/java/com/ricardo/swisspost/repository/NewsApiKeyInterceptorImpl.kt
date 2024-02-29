package com.ricardo.swisspost.repository

import com.ricardo.swisspost.di.NewsApiKey
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NewsApiKeyInterceptorImpl @Inject constructor(
    @NewsApiKey private val newApiKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val requestBuilder = original.newBuilder()
            .addHeader("X-Api-Key", newApiKey)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}