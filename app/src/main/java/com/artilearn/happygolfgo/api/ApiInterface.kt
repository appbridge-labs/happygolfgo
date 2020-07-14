package com.artilearn.happygolfgo.api

import com.artilearn.happygolfgo.data.home.HomeResponse
import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.LoginResponse
import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SplashTokenResponse
import com.artilearn.happygolfgo.data.splash.SynchronizedDataModel
import com.artilearn.happygolfgo.data.splash.SynchronizedResponse
import com.artilearn.happygolfgo.data.version.VersionResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @POST("auth/user/signIn")
    fun login(
        @Body data: HashMap<String, LoginDataModel>
    ): Single<LoginResponse>

    @POST("auth/common/token")
    fun getAccessToken(
        @Body data: HashMap<String, SplashTokenModel>
    ): Single<SplashTokenResponse>

    @POST("auth/common/uptodate")
    fun userSynchronized(
        @Body data: HashMap<String, SynchronizedDataModel>
    ): Single<SynchronizedResponse>

    @GET("main/user")
    fun mainHome(): Single<Response<HomeResponse>>

    @GET("mypage/appversion")
    fun getAppVersion(
        @Query("platform") platform: Int = 1,
        @Query("clientType") clientType: Int = 0
    ): Single<VersionResponse>

}