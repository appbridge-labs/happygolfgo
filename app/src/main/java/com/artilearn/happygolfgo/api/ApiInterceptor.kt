package com.artilearn.happygolfgo.api

import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        with(chain) {
            val request = request().newBuilder()
                .addHeader("authorization", PreferenceManager(MyApplication.getApplicationContext()).accessToken)
                .addHeader("platform", "Android")
                .build()
            proceed(request)
        }
}