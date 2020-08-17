package com.artilearn.happygolfgo.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TicketTime(
    @SerializedName("plateAvailabilityID")
    @Expose
    val plateAvailabilityId: Int?,

    @SerializedName("lessonAvailabilityID")
    @Expose
    val lessonAvailabilityId: Int?,

    @SerializedName("status")
    @Expose
    val status: Int?,

    @SerializedName("startDate")
    @Expose
    val startDate: String,

    @SerializedName("endDate")
    @Expose
    val endDate: String,

    @SerializedName("totalCount")
    @Expose
    val totalCount: Int?,

    @SerializedName("availableCount")
    @Expose
    val availableCount: Int?
) : Parcelable