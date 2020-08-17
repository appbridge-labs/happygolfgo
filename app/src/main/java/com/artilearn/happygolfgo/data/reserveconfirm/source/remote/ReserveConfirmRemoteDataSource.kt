package com.artilearn.happygolfgo.data.reserveconfirm.source.remote

import com.artilearn.happygolfgo.data.reserveconfirm.PolicyResponse
import io.reactivex.Single
import retrofit2.Response

interface ReserveConfirmRemoteDataSource {

    fun remoteRequestPolicy(): Single<Response<PolicyResponse>>
}