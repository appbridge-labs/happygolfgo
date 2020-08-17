package com.artilearn.happygolfgo.data.reserveconfirm.source

import com.artilearn.happygolfgo.data.reserveconfirm.ReserveLessonModel
import com.artilearn.happygolfgo.data.reserveconfirm.ReserveLessonResponse
import com.artilearn.happygolfgo.data.reserveconfirm.ReservePlateModel
import com.artilearn.happygolfgo.data.reserveconfirm.ReservePlateResponse
import io.reactivex.Single
import retrofit2.Response

interface ReservationRepository {

    fun reservationPlate(
        data: HashMap<String, ReservePlateModel>
    ): Single<Response<ReservePlateResponse>>

    fun reservationLesson(
        data: HashMap<String, ReserveLessonModel>
    ): Single<Response<ReserveLessonResponse>>
}