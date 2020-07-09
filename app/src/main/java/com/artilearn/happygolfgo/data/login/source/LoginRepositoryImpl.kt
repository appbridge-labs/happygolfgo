package com.artilearn.happygolfgo.data.login.source

import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.LoginResponse
import com.artilearn.happygolfgo.data.login.source.remote.LoginRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single

class LoginRepositoryImpl(
    private val loginRemoteDataSource: LoginRemoteDataSource,
    private val networkManager: NetworkManager
) : LoginRepository {

    override fun repositoryLogin(
        data: HashMap<String, LoginDataModel>
    ): Single<LoginResponse> {
        return if (networkManager.networkState()) {
            loginRemoteDataSource.remoteLogin(data)
                .flatMap {
                    Single.just(it)
                }
        } else {
            Single.error(IllegalStateException("Network Error"))
        }
    }
}