package com.artilearn.happygolfgo.data

import com.artilearn.happygolfgo.data.log.EmployeeDetail
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Log(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("body")
    @Expose
    val body: String,

    @SerializedName("comment")
    @Expose
    val comment: String?,

    @SerializedName("videoURL")
    @Expose
    val videoURL: String?,

    @SerializedName("thumbnailURL")
    @Expose
    val thumbnailURL: String?,

    @SerializedName("ratings")
    @Expose
    val ratings: Double?,

    @SerializedName("isReviewed")
    @Expose
    val isReviewed: Boolean,

    @SerializedName("isConfirmed")
    @Expose
    val isConfirmed: Boolean,

    @SerializedName("confirmedDate")
    @Expose
    val confirmedDate: String?,

    @SerializedName("createdAt")
    @Expose
    val createdAt: String,

    @SerializedName("employee")
    @Expose
    val employee: EmployeeDetail,

    @SerializedName("isReviewable")
    @Expose
    val isReviewable: Boolean
)