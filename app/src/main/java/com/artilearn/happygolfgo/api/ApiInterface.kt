package com.artilearn.happygolfgo.api

import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.LoginResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("auth/user/signIn")
    fun login(
        @Body data: HashMap<String, LoginDataModel>
    ): Single<LoginResponse>

}