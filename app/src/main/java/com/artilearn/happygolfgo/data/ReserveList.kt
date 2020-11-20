package com.artilearn.happygolfgo.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReserveList(
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

    @SerializedName("totalCount")
    @Expose
    val totalCount: Int,

    @SerializedName("remainingCount")
    @Expose
    val remainingCount: Int,

    @SerializedName("isUnlimited")
    @Expose
    val isUnlimited: Boolean,

    @SerializedName("isCancellable")
    @Expose
    val isCancellable: Boolean,

    @SerializedName("plateNumber")
    @Expose
    val plateNumber: Int?,

    @SerializedName("ticketID")
    @Expose
    val ticketId: Int?
) : Parcelable