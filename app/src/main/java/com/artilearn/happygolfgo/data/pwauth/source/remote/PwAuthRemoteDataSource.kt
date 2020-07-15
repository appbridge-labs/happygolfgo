package com.artilearn.happygolfgo.data.pwauth.source.remote

import com.artilearn.happygolfgo.data.pwauth.SendAuthModel
import io.reactivex.Completable

interface PwAuthRemoteDataSource {

    fun remoteSendAuthNumber(
        data: HashMap<String, SendAuthModel>
    ): Completable
}