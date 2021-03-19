package com.artilearn.happygolfgo.data.ticketmanager.source.model


data class TicketManaerModel(
    val name: String,
    val type: Int,
    val startDate: String,
    val endDate: String,
    val pauseList: List<PeriodsList>
)

data class PeriodsList(
    val startDate: String,
    val endDate: String
)