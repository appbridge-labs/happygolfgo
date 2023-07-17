package com.artilearn.happygolfgo.data.home.source

import com.artilearn.happygolfgo.data.home.*
import io.reactivex.Single
import retrofit2.Response

interface HomeRepository {
    fun requestHomeTicket(): Single<Response<HomeResponse>>
    fun requestAnnouncementAndExam(): Single<Response<AnnouncementAndExamResponse>>
    fun requestJoinMonthlyExam(data:HashMap<String, JoinMonthlyExamInputModel>)
            :Single<Response<JoinMonthlyExaminationResponse>>
    fun requestFreshFcmToken(data:HashMap<String, RefreshFcmTokenPutParam>)
            :Single<Response<RefreshFcmTokenResponse>>
}