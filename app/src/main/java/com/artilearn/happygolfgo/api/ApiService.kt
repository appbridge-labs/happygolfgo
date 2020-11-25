package com.artilearn.happygolfgo.api

import android.content.Context
import com.artilearn.happygolfgo.constants.CONNECT_TIMEOUT
import com.artilearn.happygolfgo.constants.READ_TIMEOUT
import com.artilearn.happygolfgo.constants.WRITE_TIMEOUT
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.PreferenceManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {
    private var applicationContext: Context = MyApplication.getApplicationContext()

    private fun interceptor(
        interceptor: ApiInterceptor
    ): OkHttpClient = OkHttpClient.Builder().run {
        connectTimeout(60, TimeUnit.SECONDS)
        readTimeout(60, TimeUnit.SECONDS)
        writeTimeout(60, TimeUnit.SECONDS)
        addInterceptor(interceptor)
        build()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(PreferenceManager(applicationContext).environmentMode)
        .client(interceptor(ApiInterceptor()))
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val api: ApiInterface = retrofit.create(ApiInterface::class.java)

}