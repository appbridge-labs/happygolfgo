package com.artilearn.happygolfgo.data.splash.source

import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SplashTokenResponse
import com.artilearn.happygolfgo.data.splash.SynchronizedDataModel
import com.artilearn.happygolfgo.data.splash.SynchronizedResponse
import io.reactivex.Single

interface SplashRepository {

    var clientSecretKey: String
    val autoLogin: Boolean
    val tutorial: Boolean

    fun getAccessToken(
        data: HashMap<String, SplashTokenModel>
    ): Single<SplashTokenResponse>

    fun userSynchronized(
        data: HashMap<String, SynchronizedDataModel>
    ): Single<SynchronizedResponse>

}