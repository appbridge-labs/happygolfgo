package com.artilearn.happygolfgo.data.alram.source

import com.artilearn.happygolfgo.data.alram.AlramResponse
import com.artilearn.happygolfgo.data.alram.source.remote.AlramRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.Response

class AlramRepositoryImpl(
    private val alramRemoteDataSource: AlramRemoteDataSource,
    private val networkManager: NetworkManager
) : AlramRepository {

    override fun getAlramMessage(): Single<Response<AlramResponse>> {
        return if (networkManager.networkState()) {
            alramRemoteDataSource.remoteGetAlramMessage()
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(IllegalStateException(it.message()))
                    }
                }
        } else {
            Single.error(IllegalStateException("Network Error"))
        }
    }
}