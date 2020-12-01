package com.artilearn.happygolfgo.ui.home.gamecenter.mapper

import android.util.Log
import com.artilearn.happygolfgo.data.gamecenter.GameCenterResponse
import com.artilearn.happygolfgo.ui.home.gamecenter.model.GCRankModel

object RankMapper : GameCenterMapper<List<GameCenterResponse.GameCenterRank>, List<GCRankModel>> {
    override fun mapper(from: List<GameCenterResponse.GameCenterRank>): List<GCRankModel> {
        return from.map { ranks ->
            GCRankModel(
                globalRank = ranks.globalRank.toString(),
                profileImage = ranks.profileImageURL,
                name = ranks.name,
                average = ranks.average.toString(),
                isMe = ranks.isMe
            )
        }
    }
}