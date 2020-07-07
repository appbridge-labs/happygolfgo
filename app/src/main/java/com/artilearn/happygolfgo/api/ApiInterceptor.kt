package com.artilearn.happygolfgo.api

import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        with(chain) {
            val request = request().newBuilder()
                .addHeader("authorization", "")
                .addHeader("platform", "Android")
                .build()
            proceed(request)
        }
}