package com.artilearn.happygolfgo.data.home.source.remote

import com.artilearn.happygolfgo.data.home.HomeResponse
import io.reactivex.Single
import retrofit2.Response

interface HomeRemoteDataSource {

    fun remoteHomeTicket(): Single<Response<HomeResponse>>
}