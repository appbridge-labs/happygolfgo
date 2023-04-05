package com.artilearn.happygolfgo.data.gamecenter.source.remote

import com.artilearn.happygolfgo.data.gamecenter.*
import com.artilearn.happygolfgo.ui.home.record.model.TSSummarySwingVideoDetailUpdateReqModel
import io.reactivex.Single
import retrofit2.Response

interface GameCenterRemoteDataSource {
    fun getGames(): Single<GameCenterResponse>
    //comments: vc45 API 추가 할때
    fun getTrainingManagementSummaryTime(): Single<Response<TrainingManagementSummaryTimeResponse>>
    fun getTrainingManagementSummaryWeight(): Single<Response<TrainingManagementSummaryWeightResponse>>
    fun getTrainingManagementSummaryGolfPower(): Single<Response<TrainingManagementSummaryGolfPowerResponse>>
    fun getTrainingManagementSummaryRound(): Single<Response<TrainingManagementSummaryRoundResponse>>
    fun getTrainingManagementSummarySwingVideo(): Single<Response<TrainingManagementSummarySwingVideoResponse>>
    fun getTrainingManagementSummaryRoundDetail(gmID:Int) : Single<Response<TrainingManagementSummaryRoundDetailResponse>>
    fun updateTrainingManagementSummarySwingVideoDetail(
       data: HashMap<String, TSSummarySwingVideoDetailUpdateReqModel>
    ): Single<Response<TrainingManagementSummarySwingVideoDetailUpdateResponse>>

    //my golf ranking in the branch and all the branches and examination report
    fun getMyGolfPowerExamResultPageInfo() : Single<Response<MyGolfPowerExamResultPageInfoResponse>>
    fun getMyGolfPowerPageExamResultPageInfoDetail(pageIndex:Int): Single<Response<MyGolfPowerExamResultPageInfoDetailResponse>>
    fun getBranchRanking(pageIndex:Int) : Single<Response<GolfRankingInBranchResponse>>
    fun getSubjectRanking(pageIndex:Int) : Single<Response<GolfRankingSubjectResponse>>
    fun getAllBranchRanking(pageIndex:Int) : Single<Response<GolfRankingAllBranchResponse>>
}