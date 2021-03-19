package com.artilearn.happygolfgo.ui.ticketmanager.pause

import com.artilearn.happygolfgo.base.Identifiable

data class PauseTicketModel(
    var ticketTitle: String,
    var ticketDate: String,
    var isStoped: Boolean
) : Identifiable {
    override val identifier: Any
        get() = ticketTitle
}