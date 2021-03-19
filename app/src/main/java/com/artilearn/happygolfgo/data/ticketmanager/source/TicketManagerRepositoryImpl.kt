package com.artilearn.happygolfgo.data.ticketmanager.source

import com.artilearn.happygolfgo.data.ticketmanager.TicketManagerResponse
import com.artilearn.happygolfgo.data.ticketmanager.source.remote.TicketManagerRemoteDataSource
import com.artilearn.happygolfgo.ui.ticketmanager.pause.mapper.PauseTicketMapper
import com.artilearn.happygolfgo.ui.ticketmanager.pause.model.PauseModel
import io.reactivex.Single

class TicketManagerRepositoryImpl(
    private val remoteDataSource: TicketManagerRemoteDataSource
) : TicketManagerRepository {
    override fun getPauseTickets(): Single<List<PauseModel>> {
        return remoteDataSource.getPauseTickets()
            .map {
                it.map(PauseTicketMapper::mapper)
            }
    }

    override fun getExpiredTickets(): Single<TicketManagerResponse> {
        return remoteDataSource.getExpiredTickets()
    }

}