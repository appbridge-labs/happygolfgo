package com.artilearn.happygolfgo.data.ticketmanager.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.ticketmanager.TicketManagerResponse
import com.artilearn.happygolfgo.data.ticketmanager.source.mapper.TicketManagerRemoteMapper
import com.artilearn.happygolfgo.data.ticketmanager.source.model.TicketManaerModel
import io.reactivex.Single

class TicketManagerRemoteDataSourceImpl(
    private val api: ApiInterface
) : TicketManagerRemoteDataSource {
    override fun getPauseTickets(): Single<List<TicketManaerModel>> {
        return api.getPauseTickets()
            .map { it.data.tickets.map(TicketManagerRemoteMapper::mapper) }
    }

    override fun getExpiredTickets(): Single<TicketManagerResponse> {
        return api.getExpiredTickets()
    }
}