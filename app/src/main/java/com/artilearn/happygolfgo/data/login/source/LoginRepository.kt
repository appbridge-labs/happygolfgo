package com.artilearn.happygolfgo.data.login.source

import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.LoginResponse
import io.reactivex.Single

interface LoginRepository {

    fun repositoryLogin(
        data: HashMap<String, LoginDataModel>
    ): Single<LoginResponse>
}