package com.artilearn.happygolfgo.data.reserveconfirm.source

import com.artilearn.happygolfgo.data.reserveconfirm.*
import com.artilearn.happygolfgo.data.reserveconfirm.source.remote.ReservationRemoteDataSource
import com.artilearn.happygolfgo.data.reserveconfirm.source.remote.ReserveConfirmRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.Response

class ReserveConfirmRepositoryImpl(
    private val reserveConfirmRemoteDataSource: ReserveConfirmRemoteDataSource,
    private val reservationRemoteDataSource: ReservationRemoteDataSource,
    private val networkManager: NetworkManager
) : ReserveConfirmRepository, ReservationRepository {

    override fun requestPolicy(): Single<Response<PolicyResponse>> {
        return if (networkManager.networkState()) {
            reserveConfirmRemoteDataSource.remoteRequestPolicy()
                .flatMap {
                    if (it.isSuccessful) {
                      Single.just(it)
                    } else {
                        Single.error(IllegalStateException("Server Error"))
                    }
                }
        } else {
            Single.error(IllegalStateException("Network Error"))
        }
    }

    override fun reservationPlate(
        data: HashMap<String, ReservePlateModel>
    ): Single<Response<ReservePlateResponse>> {
        return if (networkManager.networkState()) {
            reservationRemoteDataSource.remoteReservationPlate(data)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(IllegalStateException(it.message()))
                    }
                }
        } else {
            Single.error(IllegalStateException("Network Error"))
        }
    }

    override fun reservationLesson(
        data: HashMap<String, ReserveLessonModel>
    ): Single<Response<ReserveLessonResponse>> {
        return if (networkManager.networkState()) {
            reservationRemoteDataSource.remoteReservationLesson(data)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(IllegalStateException(it.message()))
                    }
                }
        } else {
            Single.error(IllegalStateException("Network Error"))
        }
    }
}