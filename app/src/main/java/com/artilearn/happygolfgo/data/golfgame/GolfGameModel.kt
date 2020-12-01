package com.artilearn.happygolfgo.data.golfgame

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GolfGameInfo(
    @SerializedName("games")
    @Expose
    val games: List<GolfGameModel>,

    @SerializedName("average")
    @Expose
    val average: String,

    @SerializedName("games")
    @Expose
    val bestScore: Int
)

data class GolfGameModel(
    @SerializedName("score")
    @Expose
    val score: Int,

    @SerializedName("createdAt")
    @Expose
    val date: String
)