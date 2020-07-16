package com.artilearn.happygolfgo.data.calendar.source.remote

import com.artilearn.happygolfgo.data.calendar.CalendarResponse
import io.reactivex.Single
import retrofit2.Response

interface CalendarRemoteDataSource {

    fun remoteSelectPlateDate(
        ticketId: Int
    ): Single<Response<CalendarResponse>>

    fun remoteSelectLessonDate(
        ticketId: Int
    ): Single<Response<CalendarResponse>>
}