package com.artilearn.happygolfgo.data.splash.source

import android.util.Log
import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SplashTokenResponse
import com.artilearn.happygolfgo.data.splash.source.local.SplashLocalDataSource
import com.artilearn.happygolfgo.data.splash.source.remote.SplashRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single

class SplashRepositoryImpl(
    private val splashLocalDataSource: SplashLocalDataSource,
    private val splashRemoteDataSource: SplashRemoteDataSource,
    private val networkManager: NetworkManager
) : SplashRepository {
    private val TAG = javaClass.simpleName

    override var clientSecretKey: String
        get() = splashLocalDataSource.clientSecretKey
        set(value) {
            splashLocalDataSource.clientSecretKey = value
        }

    override val autoLogin: Boolean
        get() = splashLocalDataSource.autoLogin

    override val tutorial: Boolean
        get() = splashLocalDataSource.tutorial

    override fun getAccessToken(
        data: HashMap<String, SplashTokenModel>
    ): Single<SplashTokenResponse> {
        return if (networkManager.networkState()) {
            splashRemoteDataSource.remoteGetAccessToken(data)
                .flatMap {
                    Log.d(TAG, "getAccessToken: $it")
                    splashLocalDataSource.clientSecretKey = it.data.accessToken
                    Single.just(it)
                }
        } else {
            Single.error(IllegalStateException("Network Error"))
        }
    }
}