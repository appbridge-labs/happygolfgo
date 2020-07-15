package com.artilearn.happygolfgo.data.pwauth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PwAuthResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String
)