package com.artilearn.happygolfgo.data.splash

import com.artilearn.happygolfgo.data.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SynchronizedDetail(
    @SerializedName("user")
    @Expose
    val user: User
)