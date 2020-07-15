package com.artilearn.happygolfgo.util

import android.app.Application
import android.content.Context
import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.api.ApiService
import com.artilearn.happygolfgo.data.home.source.HomeRepository
import com.artilearn.happygolfgo.data.home.source.HomeRepositoryImpl
import com.artilearn.happygolfgo.data.home.source.remote.HomeRemoteDataSource
import com.artilearn.happygolfgo.data.home.source.remote.HomeRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.login.source.LoginRepository
import com.artilearn.happygolfgo.data.login.source.LoginRepositoryImpl
import com.artilearn.happygolfgo.data.login.source.local.LoginLocalDataSource
import com.artilearn.happygolfgo.data.login.source.local.LoginLocalDataSourceImpl
import com.artilearn.happygolfgo.data.login.source.remote.LoginRemoteDataSource
import com.artilearn.happygolfgo.data.login.source.remote.LoginRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.myinfo.source.MyInfoRepository
import com.artilearn.happygolfgo.data.myinfo.source.MyInfoRepositoryImpl
import com.artilearn.happygolfgo.data.myinfo.source.local.MyInfoLocalDataSource
import com.artilearn.happygolfgo.data.myinfo.source.local.MyInfoLocalDataSourceImpl
import com.artilearn.happygolfgo.data.myinfo.source.remote.MyInfoRemoteDataSource
import com.artilearn.happygolfgo.data.myinfo.source.remote.MyInfoRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.splash.source.SplashRepository
import com.artilearn.happygolfgo.data.splash.source.SplashRepositoryImpl
import com.artilearn.happygolfgo.data.splash.source.local.SplashLocalDataSource
import com.artilearn.happygolfgo.data.splash.source.local.SplashLocalDataSourceImpl
import com.artilearn.happygolfgo.data.splash.source.remote.SplashRemoteDataSource
import com.artilearn.happygolfgo.data.splash.source.remote.SplashRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.tutorial.source.TutorialRepository
import com.artilearn.happygolfgo.data.tutorial.source.TutorialRepositoryImpl
import com.artilearn.happygolfgo.data.tutorial.source.local.TutorialLocalDataSource
import com.artilearn.happygolfgo.data.tutorial.source.local.TutorialLocalDataSourceImpl
import com.artilearn.happygolfgo.data.version.source.VersionRepository
import com.artilearn.happygolfgo.data.version.source.VersionRepositoryImpl
import com.artilearn.happygolfgo.data.version.source.local.VersionLocalDataSource
import com.artilearn.happygolfgo.data.version.source.local.VersionLocalDataSourceImpl
import com.artilearn.happygolfgo.data.version.source.remote.VersionRemoteDataSource
import com.artilearn.happygolfgo.data.version.source.remote.VersionRemoteDataSourceImpl
import com.facebook.stetho.Stetho

class MyApplication : Application() {

    private lateinit var networkManager: NetworkManager
    private lateinit var apiInterface: ApiInterface
    private lateinit var preferenceManager: PreferenceManager

    private lateinit var loginRemoteDataSource: LoginRemoteDataSource
    private lateinit var loginLocalDataSource: LoginLocalDataSource
    private lateinit var splashLocalDataSource: SplashLocalDataSource
    private lateinit var splashRemoteDataSource: SplashRemoteDataSource
    private lateinit var tutorialLocalDataSource: TutorialLocalDataSource
    private lateinit var homeRemoteDataSource: HomeRemoteDataSource
    private lateinit var versionRemoteDataSource: VersionRemoteDataSource
    private lateinit var versionLocalDataSource: VersionLocalDataSource
    private lateinit var myinfoLocalDataSource: MyInfoLocalDataSource
    private lateinit var myinfoRemoteDataSource: MyInfoRemoteDataSource

    lateinit var loginRepository: LoginRepository
    lateinit var splashRepository: SplashRepository
    lateinit var tutorialRepository: TutorialRepository
    lateinit var homeRepository: HomeRepository
    lateinit var versionRepository: VersionRepository
    lateinit var myinfoRepository: MyInfoRepository

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
        tutorialLocalDataSource = TutorialLocalDataSourceImpl(preferenceManager)
        homeRemoteDataSource = HomeRemoteDataSourceImpl(apiInterface)
        versionRemoteDataSource = VersionRemoteDataSourceImpl(apiInterface)
        versionLocalDataSource = VersionLocalDataSourceImpl(preferenceManager)
        myinfoLocalDataSource = MyInfoLocalDataSourceImpl(preferenceManager)
        myinfoRemoteDataSource = MyInfoRemoteDataSourceImpl(apiInterface)

        loginRepository = LoginRepositoryImpl(loginLocalDataSource, loginRemoteDataSource, networkManager)
        splashRepository = SplashRepositoryImpl(splashLocalDataSource, splashRemoteDataSource, networkManager)
        tutorialRepository = TutorialRepositoryImpl(tutorialLocalDataSource)
        homeRepository = HomeRepositoryImpl(homeRemoteDataSource, networkManager)
        versionRepository = VersionRepositoryImpl(versionRemoteDataSource, versionLocalDataSource, networkManager)
        myinfoRepository = MyInfoRepositoryImpl(myinfoLocalDataSource, myinfoRemoteDataSource, networkManager)
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