package com.artilearn.happygolfgo.data.gamecenter

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrainingManagementResponse {

}

data class TrainingManagementSummaryTimeResponse(
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
    val data: TrainingManagementSummaryTimeData
) {
    data class TrainingManagementSummaryTimeData(
        @SerializedName("errorCode")
        @Expose
        val errorCode: Int,

        @SerializedName("errorMessage")
        @Expose
        val errorMessage: String,

        @SerializedName("res")
        @Expose
        val res: TrainingManagementSummaryTimeRes,
    )

    data class TrainingManagementSummaryTimeRes(
        @SerializedName("title")
        @Expose
        val title: String?,

        @SerializedName("time")
        @Expose
        val time: String?,

        @SerializedName("unit")
        @Expose
        val unit: String?,
    )
}


data class TrainingManagementSummaryWeightResponse(
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
    val data: TrainingManagementSummaryWeightData
) {
    data class TrainingManagementSummaryWeightData(
        @SerializedName("errorCode")
        @Expose
        val errorCode: Int,

        @SerializedName("errorMessage")
        @Expose
        val errorMessage: String,

        @SerializedName("res")
        @Expose
        val res: TrainingManagementSummaryWeightRes,
    )

    data class TrainingManagementSummaryWeightRes(

        @SerializedName("title")
        @Expose
        val title: String?,

        @SerializedName("subTitle")
        @Expose
        val subTitle: String?,

        @SerializedName("drange")
        @Expose
        val drange: Float?,

        @SerializedName("appr")
        @Expose
        val appr: Float?,

        @SerializedName("putt")
        @Expose
        val putt: Float?,

        @SerializedName("gpower")
        @Expose
        val gpower: Float?,

        @SerializedName("stroke")
        @Expose
        val stroke: Float?,
    )
}

data class TrainingManagementSummaryGolfPowerResponse(
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
    val data: TrainingManagementSummaryGoldPowerData
) {
    data class TrainingManagementSummaryGoldPowerData(
        @SerializedName("errorCode")
        @Expose
        val errorCode: Int,

        @SerializedName("errorMessage")
        @Expose
        val errorMessage: String,

        @SerializedName("res")
        @Expose
        val res: TrainingManagementSummaryGolfPowerRes,
    )

    data class TrainingManagementSummaryGolfPowerRes(

        @SerializedName("title")
        @Expose
        val title: String?,

        @SerializedName("subTitle")
        @Expose
        val subTitle: String?,

        @SerializedName("labelBest")
        @Expose
        val labelBest: String?,

        @SerializedName("labelAvg")
        @Expose
        val labelAvg: String?,

        @SerializedName("records")
        @Expose
        val records: List<TrainingManagementSummaryGolfPowerResponse.GolfPowerRecord>
    )

    data class GolfPowerRecord(
//    { type: "GREEN_GM", title: "그린공략", best: 0, avg: 0 },
        @SerializedName("type")
        @Expose
        val type:String?,

        @SerializedName("title")
        @Expose
        val title: String?,

        @SerializedName("best")
        @Expose
        val best: Float?,

        @SerializedName("avg")
        @Expose
        val avg: Float?,
    )
}


data class TrainingManagementSummaryRoundResponse(
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
    val data: TrainingManagementSummaryRoundData
) {
    data class TrainingManagementSummaryRoundData(
        @SerializedName("errorCode")
        @Expose
        val errorCode: Int,

        @SerializedName("errorMessage")
        @Expose
        val errorMessage: String,

        @SerializedName("res")
        @Expose
        val res: TrainingManagementSummaryRoundRes,
    )

    data class TrainingManagementSummaryRoundRes(

        @SerializedName("title")
        @Expose
        val title: String?,

        @SerializedName("subTitle")
        @Expose
        val subTitle: String?,


        @SerializedName("isNoData")
        @Expose
        val isNoData: Int?,

        @SerializedName("bestTitle")
        @Expose
        val bestTitle: String?,

        @SerializedName("bestCC")
        @Expose
        val bestCC: String?,

        @SerializedName("bestDate")
        @Expose
        val bestDate: String?,

        @SerializedName("bestScore")
        @Expose
        val bestScore: String?,

        @SerializedName("bestGmId")
        @Expose
        val bestGmId: Int?,

        @SerializedName("avgTitle")
        @Expose
        val avgTitle: String?,

        @SerializedName("avgCC")
        @Expose
        val avgCC: String?,

        @SerializedName("avgDate")
        @Expose
        val avgDate: String?,

        @SerializedName("avgScore")
        @Expose
        val avgScore: String?,

        @SerializedName("lastTitle")
        @Expose
        val lastTitle: String?,

        @SerializedName("lastCC")
        @Expose
        val lastCC: String?,

        @SerializedName("lastDate")
        @Expose
        val lastDate: String?,

        @SerializedName("lastScore")
        @Expose
        val lastScore: String?,

        @SerializedName("lastGmId")
        @Expose
        val lastGmId: Int?,

        @SerializedName("records")
        @Expose
        val records: List<TrainingManagementSummaryRoundResponse.RoundRecord>
    )

    data class RoundRecord(
        @SerializedName("gmId")
        @Expose
        val gmID:Int?,

        @SerializedName("cc")
        @Expose
        val cc: String?,

        @SerializedName("regDate")
        @Expose
        val regDate:String?,

        @SerializedName("score")
        @Expose
        val score: String?,
    )
}

data class TrainingManagementSummarySwingVideoResponse(
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
    val data: TrainingManagementSummarySwingVideodData
) {
    data class TrainingManagementSummarySwingVideodData(
        @SerializedName("errorCode")
        @Expose
        val errorCode: Int,

        @SerializedName("errorMessage")
        @Expose
        val errorMessage: String,

        @SerializedName("res")
        @Expose
        val res: TrainingManagementSummarySwingVideoRes,
    )

    data class TrainingManagementSummarySwingVideoRes(

        @SerializedName("title")
        @Expose
        val title: String?,

        @SerializedName("subTitle")
        @Expose
        val subTitle: String?,

        @SerializedName("records")
        @Expose
        val records: List<TrainingManagementSummarySwingVideoResponse.SwingVideoRecord>
    )

    data class SwingVideoRecord(
        @SerializedName("id")
        @Expose
        val id: Int?,

        @SerializedName("club")
        @Expose
        val club: String?,

        @SerializedName("dist")
        @Expose
        val dist: String?,

        @SerializedName("carry")
        @Expose
        val carry: String?,

        @SerializedName("speed")
        @Expose
        val speed: String?,

        @SerializedName("angle")
        @Expose
        val angle: String?,

        @SerializedName("backspin")
        @Expose
        val backspin: String?,

        @SerializedName("shotType")
        @Expose
        val shotType: String?,

        @SerializedName("shotImageIndex")
        @Expose
        val shotImageIndex: String?,

        @SerializedName("unit")
        @Expose
        val unit: String?,

        @SerializedName("shortUnit")
        @Expose
        val shortUnit: String?,

        @SerializedName("svrUrl1")
        @Expose
        val svrUrl1: String?,

        @SerializedName("svrUrl2")
        @Expose
        val svrUrl2: String?,

        @SerializedName("regDate")
        @Expose
        val regDate: String?,

        @SerializedName("svrUrlThumbNail1")
        @Expose
        val svrUrlThumbNail1: String?,

        @SerializedName("svrUrl2ThumbNail2")
        @Expose
        val svrUrlThumbNail2: String?,
    )
}

data class TrainingManagementSummaryRoundDetailResponse(
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
    val data: TrainingManagementSummaryRoundDetailData
) {
    data class TrainingManagementSummaryRoundDetailData(
        @SerializedName("errorCode")
        @Expose
        val errorCode: Int,

        @SerializedName("errorMessage")
        @Expose
        val errorMessage: String,

        @SerializedName("res")
        @Expose
        val res: TrainingManagementSummaryRoundDetailRes,
    )

    data class TrainingManagementSummaryRoundDetailRes(

        @SerializedName("title")
        @Expose
        val title: String?,

        @SerializedName("subTitle")
        @Expose
        val subTitle: String?,

        @SerializedName("gmID")
        @Expose
        val gmID: String?,

        @SerializedName("ccName")
        @Expose
        val ccName: String?,

        @SerializedName("score")
        @Expose
        val score: String?,

        @SerializedName("roundDate")
        @Expose
        val roundDate: String?,

        @SerializedName("avgDriverDist")
        @Expose
        val avgDriverDist: String?,

        @SerializedName("avgDriverDistTitle")
        @Expose
        val avgDriverDistTitle: String?,

        @SerializedName("avgDriverDistUnit")
        @Expose
        val avgDriverDistUnit: String?,

        @SerializedName("inCourseScore")
        @Expose
        val inCourseScore: String?,

        @SerializedName("outCourseScore")
        @Expose
        val outCourseScore: String?,

        @SerializedName("longestShot")
        @Expose
        val longestShot: String?,

        @SerializedName("longestShotTitle")
        @Expose
        val longestShotTitle: String?,

        @SerializedName("longestShotUnit")
        @Expose
        val longestShotUnit: String?,

        @SerializedName("onFairwayRate")
        @Expose
        val onFairwayRate: String?,

        @SerializedName("onFairwayRateTitle")
        @Expose
        val onFairwayRateTitle: String?,

        @SerializedName("onFairwayRateUnit")
        @Expose
        val onFairwayRateUnit: String?,

        @SerializedName("onGreenRate")
        @Expose
        val onGreenRate: String?,

        @SerializedName("onGreenRateTitle")
        @Expose
        val onGreenRateTitle: String?,

        @SerializedName("onGreenRateUnit")
        @Expose
        val onGreenRateUnit: String?,

        @SerializedName("avgPutt")
        @Expose
        val avgPutt: String?,

        @SerializedName("avgPuttTitle")
        @Expose
        val avgPuttTitle: String?,

        @SerializedName("avgPuttUnit")
        @Expose
        val avgPuttUnit: String?,

        @SerializedName("parSave")
        @Expose
        val parSave: String?,

        @SerializedName("parSaveTitle")
        @Expose
        val parSaveTitle: String?,

        @SerializedName("parSaveUnit")
        @Expose
        val parSaveUnit: String?,


    @SerializedName("records")
        @Expose
        val records: List<TrainingManagementSummaryRoundDetailResponse.RoundRecord>
    )

    data class RoundRecord(

        @SerializedName("holeIndex")
        @Expose
        val holeIndex: String?,

        @SerializedName("par")
        @Expose
        val par: String?,

        @SerializedName("score")
        @Expose
        val score: String?,

        @SerializedName("deltaScore")
        @Expose
        val deltaScore: String?,
    )
}


data class TrainingManagementSummarySwingVideoDetailUpdateResponse(
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
    val data: TrainingManagementSummarySwingVideoDetailUpdateData
) {
    data class TrainingManagementSummarySwingVideoDetailUpdateData(
        @SerializedName("errorCode")
        @Expose
        val errorCode: Int,

        @SerializedName("errorMessage")
        @Expose
        val errorMessage: String,
    )
}

