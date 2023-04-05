package com.artilearn.happygolfgo.data.home.source

import com.artilearn.happygolfgo.data.home.AnnouncementAndExamResponse
import com.artilearn.happygolfgo.data.home.HomeResponse
import com.artilearn.happygolfgo.data.home.JoinMonthlyExamInputModel
import com.artilearn.happygolfgo.data.home.JoinMonthlyExaminationResponse
import io.reactivex.Single
import retrofit2.Response

interface HomeRepository {

    fun requestHomeTicket(): Single<Response<HomeResponse>>

    fun requestAnnouncementAndExam(): Single<Response<AnnouncementAndExamResponse>>

    fun requestJoinMonthlyExam(data:HashMap<String, JoinMonthlyExamInputModel>): Single<Response<JoinMonthlyExaminationResponse>>
}