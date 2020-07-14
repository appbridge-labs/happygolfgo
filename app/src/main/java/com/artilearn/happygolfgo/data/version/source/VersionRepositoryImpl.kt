package com.artilearn.happygolfgo.data.version.source

import com.artilearn.happygolfgo.data.version.VersionResponse
import com.artilearn.happygolfgo.data.version.source.remote.VersionRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single

class VersionRepositoryImpl(
    private val versionRemoteDataSource: VersionRemoteDataSource,
    private val networkManager: NetworkManager
) : VersionRepository {

    override fun getAppVersion(
        platform: Int,
        clinentType: Int
    ): Single<VersionResponse> {
       return if (networkManager.networkState()) {
           versionRemoteDataSource.remoteGetAppVersion()
               .flatMap {
                   Single.just(it)
               }
       } else {
           Single.error(IllegalStateException("Network Error"))
       }
    }
}