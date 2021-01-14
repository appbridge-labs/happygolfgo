package com.artilearn.happygolfgo.data.gamecenter.source.remote

import com.artilearn.happygolfgo.data.gamecenter.GameCenterResponse
import io.reactivex.Single

interface GameCenterRemoteDataSource {
    fun getGames(): Single<GameCenterResponse>
}