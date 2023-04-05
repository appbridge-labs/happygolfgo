package com.artilearn.happygolfgo.data.home.source.remote

import com.artilearn.happygolfgo.data.home.AnnouncementAndExamResponse
import com.artilearn.happygolfgo.data.home.HomeResponse
import com.artilearn.happygolfgo.data.home.JoinMonthlyExamInputModel
import com.artilearn.happygolfgo.data.home.JoinMonthlyExaminationResponse
import io.reactivex.Single
import retrofit2.Response

interface HomeRemoteDataSource {

    fun remoteHomeTicket(): Single<Response<HomeResponse>>

    fun remoteAnnouncementAndExam():Single<Response<AnnouncementAndExamResponse>>

    fun remoteJoinMonthlyExam(data:HashMap<String, JoinMonthlyExamInputModel>): Single<Response<JoinMonthlyExaminationResponse>>
}