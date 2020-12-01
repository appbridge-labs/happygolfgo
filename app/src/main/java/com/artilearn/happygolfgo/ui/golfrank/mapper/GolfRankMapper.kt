package com.artilearn.happygolfgo.ui.golfrank.mapper

import android.util.Log
import com.artilearn.happygolfgo.data.golfrank.GolfRankResponse
import com.artilearn.happygolfgo.ui.golfrank.model.GolfRankModel

object GolfRankMapper : GolfRankMapperModel<GolfRankResponse.Ranks, List<GolfRankModel>> {
    private val TAG = "GolfRank"
    override fun localMapper(from: GolfRankResponse.Ranks): List<GolfRankModel> {
        return from.local.map { local ->
            Log.d(TAG, "localMapper: $local")
            GolfRankModel(
                rank = local.rank.toString(),
                profileImage = local.profileImageURL,
                nickName = local.nickname,
                averages = local.average.toString(),
                isMe = local.isMe
            )
        }
    }

    override fun globalMapper(from: GolfRankResponse.Ranks): List<GolfRankModel> {
        return from.global.map { global ->
            Log.d(TAG, "globalMapper: $global")
            GolfRankModel(
                rank = global.rank.toString(),
                profileImage = global.profileImageURL,
                nickName = global.nickname,
                averages = global.average.toString(),
                isMe = global.isMe
            )
        }
    }
}