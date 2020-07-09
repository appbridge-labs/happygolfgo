package com.artilearn.happygolfgo.data.login

import com.artilearn.happygolfgo.data.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val user: User
)