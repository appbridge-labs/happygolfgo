package com.artilearn.happygolfgo.data.splash.source

import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SplashTokenResponse
import io.reactivex.Single

interface SplashRepository {

    var clientSecretKey: String

    fun getAccessToken(
        data: HashMap<String, SplashTokenModel>
    ): Single<SplashTokenResponse>
}