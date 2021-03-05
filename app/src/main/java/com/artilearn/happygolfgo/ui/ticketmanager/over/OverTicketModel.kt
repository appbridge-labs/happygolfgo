package com.artilearn.happygolfgo.ui.ticketmanager.over

import com.artilearn.happygolfgo.base.Identifiable

data class OverTicketModel(
    var ticketTitle: String,
    var ticketDate: String,
    var type: Int
) : Identifiable {
    override val identifier: Any
        get() = ticketTitle
}