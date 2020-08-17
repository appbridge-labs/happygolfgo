package com.artilearn.happygolfgo.data.home.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.home.HomeResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class HomeRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : HomeRemoteDataSource {

    override fun remoteHomeTicket(): Single<Response<HomeResponse>> {
        return apiInterface.mainHome()
            .map {
                it
            }
    }
}