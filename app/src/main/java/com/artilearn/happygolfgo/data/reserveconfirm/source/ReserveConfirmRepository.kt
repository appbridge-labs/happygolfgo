package com.artilearn.happygolfgo.data.reserveconfirm.source

import com.artilearn.happygolfgo.data.reserveconfirm.PolicyResponse
import io.reactivex.Single
import retrofit2.Response

interface ReserveConfirmRepository {

    fun requestPolicy(): Single<Response<PolicyResponse>>
}