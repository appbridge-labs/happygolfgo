package com.artilearn.happygolfgo.data.pwauth.source

import com.artilearn.happygolfgo.data.pwauth.ConfirmAuthModel
import com.artilearn.happygolfgo.data.pwauth.GetAuthModel
import com.artilearn.happygolfgo.data.pwauth.source.remote.PwAuthRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Completable

class PwAuthRepositoryImpl(
    private val pwAuthRemoteDataSource: PwAuthRemoteDataSource,
    private val networkManager: NetworkManager
) : PwAuthRepository {

    override fun getAuthNumber(
        data: HashMap<String, GetAuthModel>
    ): Completable {
        return if (networkManager.networkState()) {
            pwAuthRemoteDataSource.remoteGetAuthNumber(data)
        } else {
            Completable.error(IllegalStateException("Network Error"))
        }
    }

    override fun confirmAuthNumber(
        data: HashMap<String, ConfirmAuthModel>
    ): Completable {
        return if (networkManager.networkState()) {
            pwAuthRemoteDataSource.remoteConfirmAuthNumber(data)
        } else {
            Completable.error(IllegalStateException("Network Error"))
        }
    }
}