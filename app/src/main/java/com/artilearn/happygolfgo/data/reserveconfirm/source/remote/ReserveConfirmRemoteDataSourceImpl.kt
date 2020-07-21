package com.artilearn.happygolfgo.data.reserveconfirm.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.reserveconfirm.PolicyResponse
import io.reactivex.Single
import retrofit2.Response

class ReserveConfirmRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : ReserveConfirmRemoteDataSource {

    override fun remoteRequestPolicy(): Single<Response<PolicyResponse>> {
        return apiInterface.requestPolicy()
            .map {
                it
            }
    }
}