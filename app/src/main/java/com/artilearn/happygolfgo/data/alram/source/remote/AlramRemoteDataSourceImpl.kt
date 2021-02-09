package com.artilearn.happygolfgo.data.alram.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.alram.AlramResponse
import io.reactivex.Single
import retrofit2.Response

class AlramRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : AlramRemoteDataSource {

    override fun remoteGetAlramMessage(): Single<Response<AlramResponse>> {
        return apiInterface.getAlramMessage()
    }

    override fun deleteAlram() {

    }

    override fun deleteAll() {

    }
}