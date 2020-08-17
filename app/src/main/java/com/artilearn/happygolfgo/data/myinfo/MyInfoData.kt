package com.artilearn.happygolfgo.data.myinfo

import com.artilearn.happygolfgo.data.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MyInfoData(
    @SerializedName("user")
    @Expose
    val user: User
)