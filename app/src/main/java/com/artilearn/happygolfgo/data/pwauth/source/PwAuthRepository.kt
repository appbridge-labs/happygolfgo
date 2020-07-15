package com.artilearn.happygolfgo.data.pwauth.source

import com.artilearn.happygolfgo.data.pwauth.SendAuthModel
import io.reactivex.Completable

interface PwAuthRepository {

    fun sendAuthNumber(
        data: HashMap<String, SendAuthModel>
    ): Completable
}