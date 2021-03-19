package com.artilearn.happygolfgo.data.ticketmanager.source.remote

import com.artilearn.happygolfgo.data.ticketmanager.TicketManagerResponse
import com.artilearn.happygolfgo.data.ticketmanager.source.model.TicketManaerModel
import io.reactivex.Single

interface TicketManagerRemoteDataSource {
    fun getPauseTickets(): Single<List<TicketManaerModel>>
    fun getExpiredTickets(): Single<TicketManagerResponse>
}