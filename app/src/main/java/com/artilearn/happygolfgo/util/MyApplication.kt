package com.artilearn.happygolfgo.util

import android.app.Application
import android.content.Context
import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.api.ApiService
import com.artilearn.happygolfgo.data.login.source.LoginRepository
import com.artilearn.happygolfgo.data.login.source.LoginRepositoryImpl
import com.artilearn.happygolfgo.data.login.source.remote.LoginRemoteDataSource
import com.artilearn.happygolfgo.data.login.source.remote.LoginRemoteDataSourceImpl
import com.facebook.stetho.Stetho

class MyApplication : Application() {

    private lateinit var networkManager: NetworkManager
    private lateinit var apiInterface: ApiInterface

    private lateinit var loginRemoteDataSource: LoginRemoteDataSource

    lateinit var loginRepository: LoginRepository

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
        inject()
    }

    private fun inject() {
        networkManager = NetworkManager(this)
        apiInterface = ApiService.api

        loginRemoteDataSource = LoginRemoteDataSourceImpl(apiInterface)

        loginRepository = LoginRepositoryImpl(loginRemoteDataSource, networkManager)
    }

    init {
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null

        fun getApplicationContext(): Context {
            return instance!!.applicationContext
        }
    }

}