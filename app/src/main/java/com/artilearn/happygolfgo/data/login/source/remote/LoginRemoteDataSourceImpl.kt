package com.artilearn.happygolfgo.data.login.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.LoginResponse
import io.reactivex.Single

class LoginRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : LoginRemoteDataSource {

    override fun remoteLogin(
        data: HashMap<String, LoginDataModel>
    ): Single<LoginResponse> {
        return apiInterface.login(data)
            .map {
                it
            }
    }

}