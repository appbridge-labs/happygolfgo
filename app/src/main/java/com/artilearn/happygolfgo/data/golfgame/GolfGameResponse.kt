package com.artilearn.happygolfgo.data.golfgame

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GolfGameResponse(
    @SerializedName("resultCode")
    @Expose
    val resultCode: Int,

    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: GolfGames
) {
    data class GolfGames(
        @SerializedName("driver")
        @Expose
        val driver: GolfGameInfo,

        @SerializedName("woodUtil")
        @Expose
        val woodUtil: GolfGameInfo,

        @SerializedName("iron")
        @Expose
        val iron: GolfGameInfo,

        @SerializedName("shortGame")
        @Expose
        val shortGame: GolfGameInfo,

        @SerializedName("putter")
        @Expose
        val putter: GolfGameInfo
    )

    data class GolfGameInfo(
        @SerializedName("games")
        @Expose
        val games: List<GolfGameModel>,

        @SerializedName("average")
        @Expose
        val average: String,

        @SerializedName("bestScore")
        @Expose
        val bestScore: Int
    )

    data class GolfGameModel(
        @SerializedName("score")
        @Expose
        val score: Float,

        @SerializedName("createdAt")
        @Expose
        val date: String
    )
}