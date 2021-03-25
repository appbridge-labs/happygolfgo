package com.artilearn.happygolfgo.data

import android.os.Parcelable
import com.artilearn.happygolfgo.base.Identifiable
import com.artilearn.happygolfgo.data.ticketmanager.TicketPausePeriodsModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ticket(
    var viewType: Int = 0,

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("type")
    @Expose
    var type: Int,

    @SerializedName("period")
    @Expose
    var period: Int,

    @SerializedName("totalCount")
    @Expose
    var totalCount: Int,

    @SerializedName("usedCount")
    @Expose
    var usedCount: Int,

    @SerializedName("onlyOnceToday")
    @Expose
    var onlyOnceToday: Boolean,

    @SerializedName("remainingCount")
    @Expose
    var remainingCount: Int,

    @SerializedName("isUnlimited")
    @Expose
    var isUnlimited: Boolean,

    @SerializedName("startDate")
    @Expose
    var startDate: String,

    @SerializedName("endDate")
    @Expose
    var endDate: String,

    @SerializedName("allowToBook")
    @Expose
    var allowToBook: Boolean?,

    @SerializedName("pausesPeriods")
    @Expose
    val pasuePeriods: List<TicketPausePeriodsModel>? = null,

    @SerializedName("totalPrice")
    @Expose
    val totalPrice: Int = 0,

    @SerializedName("maxReservationCount")
    @Expose
    val maxReservationCount: Int = 0
) : Parcelable, Identifiable {
    override val identifier: Any
        get() = id
}