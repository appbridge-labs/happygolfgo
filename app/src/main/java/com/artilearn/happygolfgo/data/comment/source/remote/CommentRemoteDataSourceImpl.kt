package com.artilearn.happygolfgo.data.comment.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.comment.CommentModel
import com.artilearn.happygolfgo.data.comment.CommentResponse
import io.reactivex.Single
import retrofit2.Response

class CommentRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : CommentRemoteDataSource {

    override fun remoteUploadComment(
        data: HashMap<String, CommentModel>
    ): Single<Response<CommentResponse>> {
        return apiInterface.uploadLessonComment(data)
            .map {
                it
            }
    }
}