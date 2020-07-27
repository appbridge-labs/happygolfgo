package com.artilearn.happygolfgo.data.comment.source

import com.artilearn.happygolfgo.data.comment.CommentModel
import com.artilearn.happygolfgo.data.comment.CommentResponse
import io.reactivex.Single
import retrofit2.Response

interface CommentRepository {

    fun uploadLessonCommet(
        data: HashMap<String, CommentModel>
    ): Single<Response<CommentResponse>>
}