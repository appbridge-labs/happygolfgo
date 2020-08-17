package com.artilearn.happygolfgo.data.log.source

import com.artilearn.happygolfgo.data.log.LogResponse
import com.artilearn.happygolfgo.data.log.source.remote.LogRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.Response

class LogRepositoryImpl(
    private val logRemoteDataSource: LogRemoteDataSource,
    private val networkManager: NetworkManager
) : LogRepository {

    override fun lessonLogDetail(
        lessonId: Int
    ): Single<Response<LogResponse>> {
        return if (networkManager.networkState()) {
            logRemoteDataSource.remoteLessonLogDetail(lessonId)
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
}