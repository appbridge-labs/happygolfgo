package com.artilearn.happygolfgo.data.calendar

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CalendarInfo(
    @SerializedName("availableDates")
    @Expose
    val availableDates: List<String>,

    @SerializedName("eventDates")
    @Expose
    val eventDates: List<String>
)