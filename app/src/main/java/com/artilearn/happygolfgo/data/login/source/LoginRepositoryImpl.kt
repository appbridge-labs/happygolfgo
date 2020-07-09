package com.artilearn.happygolfgo.data.login.source

import com.artilearn.happygolfgo.data.User
import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.LoginResponse
import com.artilearn.happygolfgo.data.login.source.local.LoginLocalDataSource
import com.artilearn.happygolfgo.data.login.source.remote.LoginRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single

class LoginRepositoryImpl(
    private val loginLocalDataSource: LoginLocalDataSource,
    private val loginRemoteDataSource: LoginRemoteDataSource,
    private val networkManager: NetworkManager
) : LoginRepository {

    override var autoLogin: Boolean
        get() = loginLocalDataSource.autoLogin
        set(value) {
            loginLocalDataSource.autoLogin = value
        }

    override val tutorial: Boolean
        get() = loginLocalDataSource.autoLogin

    override fun repositoryLogin(
        data: HashMap<String, LoginDataModel>
    ): Single<LoginResponse> {
        return if (networkManager.networkState()) {
            loginRemoteDataSource.remoteLogin(data)
                .flatMap {
                    saveLocalData(it.data.user)
                    Single.just(it)
                }
        } else {
            Single.error(IllegalStateException("Network Error"))
        }
    }

    private fun saveLocalData(user: User) {
        with(loginLocalDataSource) {
            accessToken = user.accessToken
            clientSecretKey = user.clientSecretKey
            name = user.name
            phoneNumber = user.phoneNumber
            profileImageURL = user.profileImageURL
            autoLogin = true
        }
    }
}