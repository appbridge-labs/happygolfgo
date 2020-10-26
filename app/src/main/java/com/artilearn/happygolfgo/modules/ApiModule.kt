package com.artilearn.happygolfgo.modules

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.util.PreferenceManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val apiModule = module {

    single {
        Interceptor { chain ->
            with(chain) {
                val request = request().newBuilder()
                    .addHeader("authorization", get<PreferenceManager>().accessToken)
                    .addHeader("platform", "Android")
                    .build()
                proceed(request)
            }
        }
    }

    single { OkHttpClient.Builder()
        .run {
            addInterceptor(get<Interceptor>())
                .build()
        }
    }

    single<GsonConverterFactory> { GsonConverterFactory.create() }

    single<RxJava2CallAdapterFactory> { RxJava2CallAdapterFactory.create() }

    single<ApiInterface> { get<Retrofit>().create(ApiInterface::class.java) }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(get<PreferenceManager>().environmentMode)
            .client(get())
            .addConverterFactory(get<GsonConverterFactory>())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .build()
    }
}