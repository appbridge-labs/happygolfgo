package com.artilearn.happygolfgo.data.home.source

import com.artilearn.happygolfgo.data.home.HomeResponse
import com.artilearn.happygolfgo.data.home.source.remote.HomeRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
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
                            Single.error(IllegalStateException("Server Error"))
                        }
                    }
                }
        } else {
            Single.error(IllegalStateException("Network Error"))
        }
    }
}