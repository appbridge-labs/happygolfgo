package com.artilearn.happygolfgo.api

import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.LoginResponse
import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SplashTokenResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("auth/user/signIn")
    fun login(
        @Body data: HashMap<String, LoginDataModel>
    ): Single<LoginResponse>

    @POST("auth/common/token")
    fun getAccessToken(
        @Body data: HashMap<String, SplashTokenModel>
    ): Single<SplashTokenResponse>

}