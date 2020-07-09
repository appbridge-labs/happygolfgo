package com.artilearn.happygolfgo.data.splash.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SplashTokenResponse
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
}