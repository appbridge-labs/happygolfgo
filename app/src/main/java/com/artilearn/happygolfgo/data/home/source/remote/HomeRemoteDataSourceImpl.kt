package com.artilearn.happygolfgo.data.home.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.home.AnnouncementAndExamResponse
import com.artilearn.happygolfgo.data.home.HomeResponse
import com.artilearn.happygolfgo.data.home.JoinMonthlyExamInputModel
import com.artilearn.happygolfgo.data.home.JoinMonthlyExaminationResponse
import io.reactivex.Single
import retrofit2.Response

class HomeRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : HomeRemoteDataSource {

    override fun remoteHomeTicket(): Single<Response<HomeResponse>> {
        return apiInterface.mainHome()
    }

    override fun remoteAnnouncementAndExam(): Single<Response<AnnouncementAndExamResponse>> {
        return apiInterface.announcementAndExam()
    }

    override fun remoteJoinMonthlyExam( data: HashMap<String, JoinMonthlyExamInputModel>)
    :Single<Response<JoinMonthlyExaminationResponse>> {
        return apiInterface.joinMonthlyExam(data)
    }
}