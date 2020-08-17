package com.artilearn.happygolfgo.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("type")
    @Expose
    val type: Int,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("profileImageURL")
    @Expose
    val profileImageURL: String,

    @SerializedName("operatingSystem")
    @Expose
    val operatingSystem: Int,

    @SerializedName("sex")
    @Expose
    val sex: Int,

    @SerializedName("nickname")
    @Expose
    val nickname: String,

    @SerializedName("dateOfBirth")
    @Expose
    val dateOfBirth: String,

    @SerializedName("phoneNumber")
    @Expose
    val phoneNumber: String,

    @SerializedName("branchID")
    @Expose
    val branchID: Int,

    @SerializedName("clientSecretKey")
    @Expose
    val clientSecretKey: String,

    @SerializedName("fcmToken")
    @Expose
    val fcmToken: String,

    @SerializedName("isLoggedIn")
    @Expose
    val isLoggedIn: Int,

    @SerializedName("sysLang")
    @Expose
    val sysLang: Int,

    @SerializedName("memo")
    @Expose
    val memo: String,

    @SerializedName("accessToken")
    @Expose
    val accessToken: String
)