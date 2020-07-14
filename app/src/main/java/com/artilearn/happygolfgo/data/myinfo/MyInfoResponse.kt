package com.artilearn.happygolfgo.data.myinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MyInfoResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: Int,

    @SerializedName("Data")
    @Expose
    val data: MyInfoData
)