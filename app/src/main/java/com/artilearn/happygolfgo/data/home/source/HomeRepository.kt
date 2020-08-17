package com.artilearn.happygolfgo.data.home.source

import com.artilearn.happygolfgo.data.home.HomeResponse
import io.reactivex.Single
import retrofit2.Response

interface HomeRepository {

    fun requestHomeTicket(): Single<Response<HomeResponse>>
}