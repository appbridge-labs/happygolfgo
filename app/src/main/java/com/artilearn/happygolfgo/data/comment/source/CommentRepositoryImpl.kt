package com.artilearn.happygolfgo.data.comment.source

import com.artilearn.happygolfgo.data.comment.CommentModel
import com.artilearn.happygolfgo.data.comment.CommentResponse
import com.artilearn.happygolfgo.data.comment.source.remote.CommentRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.Response

class CommentRepositoryImpl(
    private val commentRemoteDataSource: CommentRemoteDataSource,
    private val networkManager: NetworkManager
) : CommentRepository {

    override fun uploadLessonCommet(
        data: HashMap<String, CommentModel>
    ): Single<Response<CommentResponse>> {
        return if (networkManager.networkState()) {
            commentRemoteDataSource.remoteUploadComment(data)
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