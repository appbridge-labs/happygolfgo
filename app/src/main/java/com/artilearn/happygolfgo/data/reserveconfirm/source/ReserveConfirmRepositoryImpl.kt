package com.artilearn.happygolfgo.data.reserveconfirm.source

import com.artilearn.happygolfgo.data.reserveconfirm.PolicyResponse
import com.artilearn.happygolfgo.data.reserveconfirm.source.remote.ReserveConfirmRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.Response

class ReserveConfirmRepositoryImpl(
    private val reserveConfirmRemoteDataSource: ReserveConfirmRemoteDataSource,
    private val networkManager: NetworkManager
) : ReserveConfirmRepository {

    override fun requestPolicy(): Single<Response<PolicyResponse>> {
        return if (networkManager.networkState()) {
            reserveConfirmRemoteDataSource.remoteRequestPolicy()
                .flatMap {
                    if (it.isSuccessful) {
                      Single.just(it)
                    } else {
                        Single.error(IllegalStateException("Server Error"))
                    }
                }
        } else {
            Single.error(IllegalStateException("Network Error"))
        }
    }
}