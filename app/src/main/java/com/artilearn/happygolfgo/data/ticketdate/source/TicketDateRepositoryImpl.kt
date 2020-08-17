package com.artilearn.happygolfgo.data.ticketdate.source

import com.artilearn.happygolfgo.data.ticketdate.TicketDateResponse
import com.artilearn.happygolfgo.data.ticketdate.source.remote.TicketDateRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.Response

class TicketDateRepositoryImpl(
    private val calendarRemoteDataSource: TicketDateRemoteDataSource,
    private val networkManager: NetworkManager
) : TicketDateRepository {

    override fun selectPlateDate(
        ticketId: Int
    ): Single<Response<TicketDateResponse>> {
        return if (networkManager.networkState()) {
            calendarRemoteDataSource.remoteSelectPlateDate(ticketId)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(IllegalStateException(it.message()))
                    }
                }
        } else {
            Single.error(IllegalStateException("Network Error"))
        }
    }

    override fun selectLessonDate(
        ticketId: Int
    ): Single<Response<TicketDateResponse>> {
        return if (networkManager.networkState()) {
            calendarRemoteDataSource.remoteSelectLessonDate(ticketId)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(IllegalStateException(it.message()))
                    }
                }
        } else {
            Single.error(IllegalStateException("Network Error"))
        }
    }
}