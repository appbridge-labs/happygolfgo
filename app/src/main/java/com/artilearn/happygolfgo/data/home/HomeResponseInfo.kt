package com.artilearn.happygolfgo.data.home

import com.artilearn.happygolfgo.data.Ticket
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeResponseInfo(
    @SerializedName("Tickets")
    @Expose
    val tickets: List<Ticket>,

    @SerializedName("PausedTickets")
    @Expose
    val pausedTickets: List<PuaseTicketModel>,

    @SerializedName("ExpiredTickets")
    @Expose
    val expiredTickets: List<ExpiredTicketsModel>
)

data class PuaseTicketModel(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("type")
    @Expose
    val type: Int,

    @SerializedName("period")
    @Expose
    val period: Int,

    @SerializedName("totalCount")
    @Expose
    val totalCount: Int,

    @SerializedName("usedCount")
    @Expose
    val usedCount: Int,

    @SerializedName("onlyOnceToday")
    @Expose
    val onlyOnceToday: Boolean,

    @SerializedName("remainingCount")
    @Expose
    val remainingCount: Int,

    @SerializedName("isUnlimited")
    @Expose
    val isUnlimited: Boolean,

    @SerializedName("startDate")
    @Expose
    val startDate: String,

    @SerializedName("endDate")
    @Expose
    val endDate: String,

    @SerializedName("allowToBook")
    @Expose
    val allowToBook: Boolean?
)

data class TicketPausePeriodsModel(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("ticketID")
    @Expose
    val ticketId: Int,

    @SerializedName("startDate")
    @Expose
    val startDate: String,

    @SerializedName("endDate")
    @Expose
    val endDate: String
)

data class ExpiredTicketsModel(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("type")
    @Expose
    val type: Int,

    @SerializedName("period")
    @Expose
    val period: Int,

    @SerializedName("totalCount")
    @Expose
    val totalCount: Int,

    @SerializedName("usedCount")
    @Expose
    val usedCount: Int,

    @SerializedName("onlyOnceToday")
    @Expose
    val onlyOnceToday: Boolean,

    @SerializedName("remainingCount")
    @Expose
    val remainingCount: Int,

    @SerializedName("isUnlimited")
    @Expose
    val isUnlimited: Boolean,

    @SerializedName("startDate")
    @Expose
    val startDate: String,

    @SerializedName("endDate")
    @Expose
    val endDate: String,

    @SerializedName("allowToBook")
    @Expose
    val allowToBook: Boolean?,

    @SerializedName("totalPrice")
    @Expose
    val price: String
)