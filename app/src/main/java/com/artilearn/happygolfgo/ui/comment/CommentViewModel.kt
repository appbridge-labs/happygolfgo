package com.artilearn.happygolfgo.ui.comment

import android.util.Log
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.comment.CommentModel
import com.artilearn.happygolfgo.data.comment.source.CommentRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CommentViewModel(
    private val commetRepository: CommentRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    fun uploadLessonCommet() {
        val dataModel = CommentModel(1,"1",1)
        val hashMap = hashMapOf<String, CommentModel>()
        hashMap["Data"] = dataModel
        compositeDisposable.add(
            commetRepository.uploadLessonCommet(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({ response ->
                    Log.d(TAG, "uploadLessonCommet: $response")
                }, { t ->
                    Log.d(TAG, "uploadLessonCommet: ${t.message}")
                })
        )
    }
}