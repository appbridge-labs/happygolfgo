package com.artilearn.happygolfgo.data.splash.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SplashTokenResponse
import com.artilearn.happygolfgo.data.splash.SynchronizedDataModel
import com.artilearn.happygolfgo.data.splash.SynchronizedResponse
import io.reactivex.Single

class SplashRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : SplashRemoteDataSource {

    override fun remoteGetAccessToken(
        data: HashMap<String, SplashTokenModel>
    ): Single<SplashTokenResponse> {
        return apiInterface.getAccessToken(data)
            .map {
                it
            }
    }

    override fun remoteUserSynchronized(
        data: HashMap<String, SynchronizedDataModel>
    ): Single<SynchronizedResponse> {
        return apiInterface.userSynchronized(data)
            .map {
                it
            }
    }
}