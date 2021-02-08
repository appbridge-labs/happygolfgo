package com.artilearn.happygolfgo.data.alram.source.remote

import com.artilearn.happygolfgo.data.alram.AlramResponse
import io.reactivex.Single
import retrofit2.Response

interface AlramRemoteDataSource {

    fun remoteGetAlramMessage(): Single<Response<AlramResponse>>

    fun deleteAlram()
}