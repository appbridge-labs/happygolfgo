package com.artilearn.happygolfgo.data.gamecenter.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.gamecenter.GameCenterResponse
import com.artilearn.happygolfgo.data.gamecenter.source.remote.GameCenterRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single

class GameCenterRepositoryImpl(
    private val remote: GameCenterRemoteDataSource,
    private val network: NetworkManager
) : GameCenterRepository {
    override fun getGames(): Single<GameCenterResponse> {
        return if (network.networkState()) {
            remote.getGames()
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }
}