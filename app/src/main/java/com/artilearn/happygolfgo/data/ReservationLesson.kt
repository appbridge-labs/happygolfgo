package com.artilearn.happygolfgo.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReservationLesson(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("status")
    @Expose
    val status: Int,

    @SerializedName("startDate")
    @Expose
    val startDate: String,

    @SerializedName("endDate")
    @Expose
    val endDate: String,

    @SerializedName("remainingCount")
    @Expose
    val remainingCount: Int,

    @SerializedName("totalCount")
    @Expose
    val totalCount: Int,

    @SerializedName("isUnlimited")
    @Expose
    val isUnlimited: Boolean
)