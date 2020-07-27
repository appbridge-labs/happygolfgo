package com.artilearn.happygolfgo.data.comment.source.remote

import com.artilearn.happygolfgo.data.comment.CommentModel
import com.artilearn.happygolfgo.data.comment.CommentResponse
import io.reactivex.Single
import retrofit2.Response

interface CommentRemoteDataSource {

    fun remoteUploadComment(
        data: HashMap<String, CommentModel>
    ): Single<Response<CommentResponse>>
}