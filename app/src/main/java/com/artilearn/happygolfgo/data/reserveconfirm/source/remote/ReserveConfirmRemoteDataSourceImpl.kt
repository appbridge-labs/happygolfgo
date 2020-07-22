package com.artilearn.happygolfgo.data.reserveconfirm.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.reserveconfirm.*
import io.reactivex.Single
import retrofit2.Response

class ReserveConfirmRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : ReserveConfirmRemoteDataSource, ReservationRemoteDataSource {

    override fun remoteRequestPolicy(): Single<Response<PolicyResponse>> {
        return apiInterface.requestPolicy()
            .map {
                it
            }
    }

    override fun remoteReservationPlate(
        data: HashMap<String, ReservePlateModel>
    ): Single<Response<ReservePlateResponse>> {
        return apiInterface.reservationPlate(data)
            .map {
                it
            }
    }

    override fun remoteReservationLesson(
        data: HashMap<String, ReserveLessonModel>
    ): Single<Response<ReserveLessonResponse>> {
        return apiInterface.reservationLessn(data)
            .map {
                it
            }
    }
}