package com.artilearn.happygolfgo.data.version.source

import com.artilearn.happygolfgo.data.version.VersionResponse
import com.artilearn.happygolfgo.data.version.source.local.VersionLocalDataSource
import com.artilearn.happygolfgo.data.version.source.remote.VersionRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import com.artilearn.happygolfgo.util.PreferenceManager
import io.reactivex.Completable
import io.reactivex.Single

class VersionRepositoryImpl(
    private val versionRemoteDataSource: VersionRemoteDataSource,
    private val versionLocalDataSource: VersionLocalDataSource,
    private val preferenceManager: PreferenceManager,
    private val networkManager: NetworkManager
) : VersionRepository {

    override fun getAppVersion(
        platform: Int,
        clinentType: Int
    ): Single<VersionResponse> {
       return if (networkManager.networkState()) {
           versionRemoteDataSource.remoteGetAppVersion()
               .flatMap {
                   versionLocalDataSource.appVersion = it.data.latestVersion
                   Single.just(it)
               }
       } else {
           Single.error(IllegalStateException("Network Error"))
       }
    }

    override fun userLogout(): Completable {
        return if (networkManager.networkState()) {
            versionRemoteDataSource.userLogout()
                .doOnComplete { preferenceManager.userLogout() }
        } else {
            Completable.error(IllegalStateException("Network Error"))
        }
    }
}