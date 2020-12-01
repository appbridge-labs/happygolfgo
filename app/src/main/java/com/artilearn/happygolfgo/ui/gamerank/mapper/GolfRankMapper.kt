package com.artilearn.happygolfgo.ui.gamerank.mapper

import com.artilearn.happygolfgo.data.golfrank.GolfRankResponse
import com.artilearn.happygolfgo.ui.gamerank.model.GolfRankModel

object GolfRankMapper : GolfRankMapperModel<GolfRankResponse.Ranks, List<GolfRankModel>> {
    override fun localMapper(from: GolfRankResponse.Ranks): List<GolfRankModel> {
        return from.local.map { local ->
            GolfRankModel(
                rank = local.rank.toString(),
                profileImage = local.profileImageURL,
                nickName = local.nickname,
                averages = local.average.toString()
            )
        }
    }

    override fun globalMapper(from: GolfRankResponse.Ranks): List<GolfRankModel> {
        return from.global.map { global ->
            GolfRankModel(
                rank = global.rank.toString(),
                profileImage = global.profileImageURL,
                nickName = global.nickname,
                averages = global.average.toString()
            )
        }
    }
}