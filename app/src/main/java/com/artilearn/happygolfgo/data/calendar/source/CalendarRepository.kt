package com.artilearn.happygolfgo.data.calendar.source

import com.artilearn.happygolfgo.data.calendar.CalendarResponse
import io.reactivex.Single
import retrofit2.Response

interface CalendarRepository {

    fun selectPlateDate(
        ticketId: Int
    ): Single<Response<CalendarResponse>>

    fun selectLessonDate(
        ticketId: Int
    ): Single<Response<CalendarResponse>>
}