package com.artilearn.happygolfgo.data.calendar.source

import com.artilearn.happygolfgo.data.calendar.CalendarResponse
import com.artilearn.happygolfgo.data.calendar.source.remote.CalendarRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.Response

class CalendarRepositoryImpl(
    private val calendarRemoteDataSource: CalendarRemoteDataSource,
    private val networkManager: NetworkManager
) : CalendarRepository {

    override fun selectPlateDate(
        ticketId: Int
    ): Single<Response<CalendarResponse>> {
        return if (networkManager.networkState()) {
            calendarRemoteDataSource.remoteSelectPlateDate(ticketId)
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

    override fun selectLessonDate(
        ticketId: Int
    ): Single<Response<CalendarResponse>> {
        return if (networkManager.networkState()) {
            calendarRemoteDataSource.remoteSelectLessonDate(ticketId)
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