package com.artilearn.happygolfgo.data.ticketdate.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.ticketdate.TicketDateResponse
import com.artilearn.happygolfgo.data.ticketdate.source.remote.TicketDateRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.HttpException
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
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
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
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }
}