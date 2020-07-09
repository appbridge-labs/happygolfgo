package com.artilearn.happygolfgo.data.splash.source.remote

import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SplashTokenResponse
import com.artilearn.happygolfgo.data.splash.SynchronizedDataModel
import com.artilearn.happygolfgo.data.splash.SynchronizedResponse
import io.reactivex.Single

interface SplashRemoteDataSource {

    fun remoteGetAccessToken(
        data: HashMap<String, SplashTokenModel>
    ): Single<SplashTokenResponse>

    fun remoteUserSynchronized(
        data: HashMap<String, SynchronizedDataModel>
    ): Single<SynchronizedResponse>
}