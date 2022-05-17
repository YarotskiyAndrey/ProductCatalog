package com.task.productcatalog.data.api

import com.task.productcatalog.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("x-apikey", BuildConfig.API_KEY)
            .build()
        return chain.proceed(request)
    }
}
