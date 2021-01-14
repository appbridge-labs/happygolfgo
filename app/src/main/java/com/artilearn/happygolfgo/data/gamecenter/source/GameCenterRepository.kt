package com.artilearn.happygolfgo.data.gamecenter.source

import com.artilearn.happygolfgo.data.gamecenter.GameCenterResponse
import io.reactivex.Single

interface GameCenterRepository {
    fun getGames(): Single<GameCenterResponse>
}