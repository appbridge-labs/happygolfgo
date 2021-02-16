package com.artilearn.happygolfgo.ui.ticketmanager.use

import com.artilearn.happygolfgo.base.Identifiable

data class UseTicketModel(
    var ticketTitle: String,
    var ticketDate: String,
    var isStoped: Boolean
) : Identifiable {
    override val identifier: Any
        get() = ticketTitle
}