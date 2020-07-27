package com.artilearn.happygolfgo.data.log

import com.artilearn.happygolfgo.data.Log
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LogDetail(
    @SerializedName("lessonLog")
    @Expose
    val lessonLog: Log
)