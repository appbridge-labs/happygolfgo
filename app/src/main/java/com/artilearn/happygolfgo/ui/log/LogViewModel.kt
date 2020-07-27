package com.artilearn.happygolfgo.ui.log

import android.util.Log
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.log.source.LogRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LogViewModel(
    private val logRepository: LogRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName


    fun requestLessonLogDetail(lessonId: Int) {
        compositeDisposable.add(
            logRepository.lessonLogDetail(lessonId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({ response ->
                    Log.d(TAG, "requestLessonLogDetail: $response")
                }, { t ->
                    Log.d(TAG, "requestLessonLogDetail: ${t.message}")
                })
        )
    }

}