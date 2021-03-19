package com.artilearn.happygolfgo.ui.ticketmanager.expired

import com.artilearn.happygolfgo.base.Identifiable

data class ExpiredTicketModel(
    var ticketTitle: String,
    var ticketDate: String,
    var type: Int
) : Identifiable {
    override val identifier: Any
        get() = ticketTitle
}