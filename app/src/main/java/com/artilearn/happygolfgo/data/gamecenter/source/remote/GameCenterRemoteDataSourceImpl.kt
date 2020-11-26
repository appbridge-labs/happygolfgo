package com.artilearn.happygolfgo.data.gamecenter.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.gamecenter.GameCenterResponse
import io.reactivex.Single

class GameCenterRemoteDataSourceImpl(
    private val api: ApiInterface
) : GameCenterRemoteDataSource {
    override fun getGames(): Single<GameCenterResponse> {
        return api.getGameCenter()
    }
}