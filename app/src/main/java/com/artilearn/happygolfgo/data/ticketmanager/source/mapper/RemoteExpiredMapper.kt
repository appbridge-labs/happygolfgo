package com.artilearn.happygolfgo.data.ticketmanager.source.mapper

import com.artilearn.happygolfgo.data.ticketmanager.TicketModelItem
import com.artilearn.happygolfgo.data.ticketmanager.source.model.RemoteExpiredTicketModel
import com.artilearn.happygolfgo.mapper.Mapper

object RemoteExpiredMapper : Mapper<TicketModelItem, RemoteExpiredTicketModel> {
    override fun mapper(item: TicketModelItem): RemoteExpiredTicketModel {
        return RemoteExpiredTicketModel(
            name = item.name,
            startDate = item.startDate,
            endDate = item.endDate,
            price = item.totalPrice
        )
    }
}