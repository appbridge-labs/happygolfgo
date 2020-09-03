package com.artilearn.happygolfgo.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Alram(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("body")
    @Expose
    val body: String,

    @SerializedName("type")
    @Expose
    val type: Int,

    @SerializedName("createdAt")
    @Expose
    val createdAt: String,

    @SerializedName("eventType")
    @Expose
    val eventType: String,

    @SerializedName("eventValue")
    @Expose
    val eventValue: String,

    @SerializedName("isRead")
    @Expose
    val isRead: Boolean
)