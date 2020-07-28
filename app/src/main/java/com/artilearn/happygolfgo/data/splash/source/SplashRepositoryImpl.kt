package com.artilearn.happygolfgo.data.splash.source

import com.artilearn.happygolfgo.data.User
import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SplashTokenResponse
import com.artilearn.happygolfgo.data.splash.SynchronizedDataModel
import com.artilearn.happygolfgo.data.splash.SynchronizedResponse
import com.artilearn.happygolfgo.data.splash.source.local.SplashLocalDataSource
import com.artilearn.happygolfgo.data.splash.source.remote.SplashRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.Response

class SplashRepositoryImpl(
    private val splashLocalDataSource: SplashLocalDataSource,
    private val splashRemoteDataSource: SplashRemoteDataSource,
    private val networkManager: NetworkManager
) : SplashRepository {

    override var clientSecretKey: String
        get() = splashLocalDataSource.clientSecretKey
        set(value) {
            splashLocalDataSource.clientSecretKey = value
        }

    override val autoLogin: Boolean
        get() = splashLocalDataSource.autoLogin

    override val tutorial: Boolean
        get() = splashLocalDataSource.tutorial

    override val fcmToken: String?
        get() = splashLocalDataSource.fcmToken

    override fun getAccessToken(
        data: HashMap<String, SplashTokenModel>
    ): Single<Response<SplashTokenResponse>> {
        return if (networkManager.networkState()) {
            splashRemoteDataSource.remoteGetAccessToken(data)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(IllegalStateException("Server Error"))
                    }
                }
        } else {
            Single.error(IllegalStateException("Network Error"))
        }
    }

    override fun userSynchronized(
        data: HashMap<String, SynchronizedDataModel>
    ): Single<Response<SynchronizedResponse>> {
        return if (networkManager.networkState()) {
            splashRemoteDataSource.remoteUserSynchronized(data)
                .flatMap {
                    val body = it.body()
                    if (it.isSuccessful && body != null) {
                        userInfoSynchronized(body.data.user)
                        Single.just(it)
                    } else {
                        Single.error(IllegalStateException("Server Error"))
                    }
                }
        } else {
            Single.error(IllegalStateException("Network Error"))
        }
    }

    private fun userInfoSynchronized(user: User) {
        with(splashLocalDataSource) {
            name = user.name
            profileImageURL = user.profileImageURL
            nickname = user.nickname
            clientSecretKey = user.clientSecretKey
            fcmToken = user.fcmToken
            accessToken = user.accessToken
            dateOfBirth = user.dateOfBirth
        }
    }
}