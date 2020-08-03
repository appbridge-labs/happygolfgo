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

//todo 개발자 옵션만 마무리하면 끝!
object ApiService {
    private var applicationContext: Context = MyApplication.getApplicationContext()

    private fun interceptor(
        interceptor: ApiInterceptor
    ): OkHttpClient = OkHttpClient.Builder().run {
        connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        addInterceptor(interceptor)
        build()
    }
//http://52.79.253.238/development/appClient/
    private val retrofit = Retrofit.Builder()
        .baseUrl(PreferenceManager(applicationContext).environmentMode)
        .client(interceptor(ApiInterceptor()))
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val api: ApiInterface = retrofit.create(ApiInterface::class.java)

}