package com.artilearn.happygolfgo.data.gamecenter.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.gamecenter.*
import com.artilearn.happygolfgo.ui.home.record.model.TSSummarySwingVideoDetailUpdateReqModel
import io.reactivex.Single
import retrofit2.Response

class GameCenterRemoteDataSourceImpl(
    private val api: ApiInterface
) : GameCenterRemoteDataSource {
    override fun getGames(): Single<GameCenterResponse> {
        return api.getGameCenter()
    }
    //comments:  vc45 API  추가
    override fun getTrainingManagementSummaryTime(): Single<Response<TrainingManagementSummaryTimeResponse>> {
        return api.getTrainingManagementSummaryTime()
    }

    override fun getTrainingManagementSummaryWeight(): Single<Response<TrainingManagementSummaryWeightResponse>> {
        return api.getTrainingManagementSummaryWeight()
    }

    override fun getTrainingManagementSummaryGolfPower(): Single<Response<TrainingManagementSummaryGolfPowerResponse>> {
        return api.getTrainingManagementSummaryGolfPower()
    }

    override fun getTrainingManagementSummaryRound(): Single<Response<TrainingManagementSummaryRoundResponse>> {
        return api.getTrainingManagementSummaryRound()
    }

    override fun getTrainingManagementSummarySwingVideo(): Single<Response<TrainingManagementSummarySwingVideoResponse>> {
        return api.getTrainingManagementSummarySwingVideo()
    }

    override fun getTrainingManagementSummaryRoundDetail(gmID: Int): Single<Response<TrainingManagementSummaryRoundDetailResponse>> {
        return api.getTrainingManagementSummaryRoundDetail(gmID)
    }

    override fun updateTrainingManagementSummarySwingVideoDetail(data: HashMap<String, TSSummarySwingVideoDetailUpdateReqModel>): Single<Response<TrainingManagementSummarySwingVideoDetailUpdateResponse>> {
        return api.updateTrainingManagementSummarySwingVideoDetail(data);
    }

}