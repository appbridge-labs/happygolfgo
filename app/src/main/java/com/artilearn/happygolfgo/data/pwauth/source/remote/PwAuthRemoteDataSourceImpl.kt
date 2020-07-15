package com.artilearn.happygolfgo.data.pwauth.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.pwauth.SendAuthModel
import io.reactivex.Completable

class PwAuthRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : PwAuthRemoteDataSource {

    override fun remoteSendAuthNumber(
        data: HashMap<String, SendAuthModel>
    ): Completable {
        return apiInterface.sendAuthNumber(data)
    }
}