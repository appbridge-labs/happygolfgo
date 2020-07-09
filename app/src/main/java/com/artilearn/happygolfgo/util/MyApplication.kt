package com.artilearn.happygolfgo.util

import android.app.Application
import android.content.Context
import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.api.ApiService
import com.artilearn.happygolfgo.data.login.source.LoginRepository
import com.artilearn.happygolfgo.data.login.source.LoginRepositoryImpl
import com.artilearn.happygolfgo.data.login.source.local.LoginLocalDataSource
import com.artilearn.happygolfgo.data.login.source.local.LoginLocalDataSourceImpl
import com.artilearn.happygolfgo.data.login.source.remote.LoginRemoteDataSource
import com.artilearn.happygolfgo.data.login.source.remote.LoginRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.splash.source.SplashRepository
import com.artilearn.happygolfgo.data.splash.source.SplashRepositoryImpl
import com.artilearn.happygolfgo.data.splash.source.local.SplashLocalDataSource
import com.artilearn.happygolfgo.data.splash.source.local.SplashLocalDataSourceImpl
import com.artilearn.happygolfgo.data.splash.source.remote.SplashRemoteDataSource
import com.artilearn.happygolfgo.data.splash.source.remote.SplashRemoteDataSourceImpl
import com.facebook.stetho.Stetho

class MyApplication : Application() {

    private lateinit var networkManager: NetworkManager
    private lateinit var apiInterface: ApiInterface
    private lateinit var preferenceManager: PreferenceManager

    private lateinit var loginRemoteDataSource: LoginRemoteDataSource
    private lateinit var loginLocalDataSource: LoginLocalDataSource
    private lateinit var splashLocalDataSource: SplashLocalDataSource
    private lateinit var splashRemoteDataSource: SplashRemoteDataSource

    lateinit var loginRepository: LoginRepository
    lateinit var splashRepository: SplashRepository

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
        inject()
    }

    private fun inject() {
        networkManager = NetworkManager(this)
        apiInterface = ApiService.api
        preferenceManager = PreferenceManager(this)

        loginRemoteDataSource = LoginRemoteDataSourceImpl(apiInterface)
        loginLocalDataSource = LoginLocalDataSourceImpl(preferenceManager)
        splashLocalDataSource = SplashLocalDataSourceImpl(preferenceManager)
        splashRemoteDataSource = SplashRemoteDataSourceImpl(apiInterface)

        loginRepository = LoginRepositoryImpl(loginLocalDataSource, loginRemoteDataSource, networkManager)
        splashRepository = SplashRepositoryImpl(splashLocalDataSource, splashRemoteDataSource, networkManager)
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