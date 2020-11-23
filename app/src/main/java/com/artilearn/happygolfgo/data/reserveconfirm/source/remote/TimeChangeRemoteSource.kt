package com.artilearn.happygolfgo.data.reserveconfirm.source.remote

import com.artilearn.happygolfgo.data.reserveconfirm.PlateTimeChangeModel
import com.artilearn.happygolfgo.data.reserveconfirm.PlateTimeChangeResponse
import io.reactivex.Single

interface TimeChangeRemoteSource {

    fun plateTimeChange(
        data: HashMap<String, PlateTimeChangeModel>
    ): Single<PlateTimeChangeResponse>
}