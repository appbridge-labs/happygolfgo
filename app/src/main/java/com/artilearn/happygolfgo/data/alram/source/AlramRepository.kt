package com.artilearn.happygolfgo.data.alram.source

import com.artilearn.happygolfgo.data.alram.AlramResponse
import io.reactivex.Single
import retrofit2.Response

interface AlramRepository {

    fun getAlramMessage(): Single<Response<AlramResponse>>
}