package com.artilearn.happygolfgo.data.splash

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SynchronizedResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: Int,

    @SerializedName("Data")
    @Expose
    val data: SynchronizedDetail
)