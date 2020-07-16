package com.artilearn.happygolfgo.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LessonLog(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("createdAt")
    @Expose
    val createdAt: String,

    @SerializedName("employeeName")
    @Expose
    val employeeName: String
)