package com.artilearn.happygolfgo.data.pwauth.source

import com.artilearn.happygolfgo.data.pwauth.SendAuthModel
import com.artilearn.happygolfgo.data.pwauth.source.remote.PwAuthRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Completable

class PwAuthRepositoryImpl(
    private val pwAuthRemoteDataSource: PwAuthRemoteDataSource,
    private val networkManager: NetworkManager
) : PwAuthRepository {

    override fun sendAuthNumber(
        data: HashMap<String, SendAuthModel>
    ): Completable {
        return if (networkManager.networkState()) {
            pwAuthRemoteDataSource.remoteSendAuthNumber(data)
        } else {
            Completable.error(IllegalStateException("Network Error"))
        }
    }
}