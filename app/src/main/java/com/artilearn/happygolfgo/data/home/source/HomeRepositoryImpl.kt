package com.artilearn.happygolfgo.data.home.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.home.HomeResponse
import com.artilearn.happygolfgo.data.home.source.remote.HomeRemoteDataSource
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.EXPIRED_TICKET
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.PAUSE_TICKET
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.USE_TICKET
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.HttpException
import retrofit2.Response

class HomeRepositoryImpl(
    private val homeRemoteDataSource: HomeRemoteDataSource,
    private val networkManager: NetworkManager
) : HomeRepository {

    override fun requestHomeTicket(): Single<Response<HomeResponse>> {
        return if (networkManager.networkState()) {
            homeRemoteDataSource.remoteHomeTicket()
                .flatMap {
                    with(it) {
                        if (isSuccessful) {
                            Single.just(it)
                        } else {
                            Single.error(HttpException(it))
                        }
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }
}