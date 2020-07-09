package com.artilearn.happygolfgo.data.login.source

import android.util.Log
import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.LoginResponse
import com.artilearn.happygolfgo.data.login.source.remote.LoginRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single

class LoginRepositoryImpl(
    private val loginRemoteDataSource: LoginRemoteDataSource,
    private val networkManager: NetworkManager
) : LoginRepository {
    private val TAG = javaClass.simpleName

    override fun repositoryLogin(
        data: HashMap<String, LoginDataModel>
    ): Single<LoginResponse> {
        return if (networkManager.networkState()) {
            loginRemoteDataSource.remoteLogin(data)
                .flatMap {
                    Log.d(TAG, "repositoryLogin: $it")
                    Single.just(it)
                }
        } else {
            Single.error(IllegalStateException("Network Error"))
        }
    }
}