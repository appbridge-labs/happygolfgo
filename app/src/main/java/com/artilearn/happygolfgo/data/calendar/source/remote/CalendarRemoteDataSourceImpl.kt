package com.artilearn.happygolfgo.data.calendar.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.calendar.CalendarResponse
import io.reactivex.Single
import retrofit2.Response

class CalendarRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : CalendarRemoteDataSource {

    override fun remoteSelectPlateDate(
        ticketId: Int
    ): Single<Response<CalendarResponse>> {
        return apiInterface.selectPlateDate(ticketId)
            .map {
                it
            }
    }

    override fun remoteSelectLessonDate(
        ticketId: Int
    ): Single<Response<CalendarResponse>> {
        return apiInterface.selectLessonDate(ticketId)
            .map {
                it
            }
    }
}