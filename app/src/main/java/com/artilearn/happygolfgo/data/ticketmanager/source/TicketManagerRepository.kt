package com.artilearn.happygolfgo.data.ticketmanager.source

import com.artilearn.happygolfgo.data.ticketmanager.TicketManagerResponse
import com.artilearn.happygolfgo.data.ticketmanager.source.model.TicketManaerModel
import com.artilearn.happygolfgo.ui.ticketmanager.pause.mapper.PauseTicketMapper
import com.artilearn.happygolfgo.ui.ticketmanager.pause.model.PauseModel
import io.reactivex.Single

interface TicketManagerRepository {
    fun getPauseTickets(): Single<List<PauseModel>>
    fun getExpiredTickets(): Single<TicketManagerResponse>
}