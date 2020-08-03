package com.artilearn.happygolfgo.ui.log

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.LogDetail
import com.artilearn.happygolfgo.data.log.source.LogRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LogViewModel(
    private val logRepository: LogRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val _logDetail: SingleLiveEvent<LogDetail> = SingleLiveEvent()
    private val _videoUrl: SingleLiveEvent<String> = SingleLiveEvent()

    val logDetail: LiveData<LogDetail> get() = _logDetail
    val videoUrl: LiveData<String> get() = _videoUrl

    fun requestLessonLogDetail(lessonId: Int?) {
        lessonId?.let {
            compositeDisposable.add(
                logRepository.lessonLogDetail(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showLoading() }
                    .doAfterTerminate { hideLoading() }
                    .subscribe({ response ->
                        val body = response.body()
                        if (response.isSuccessful && body != null) {
                            _logDetail.value = body.data.lessonLog
                        } else {
                            _serverError.value = response.errorBody().toString()
                        }
                    }, { t ->
                        Log.d(TAG, "requestLessonLogDetail: ${t.message}")
                        when (t.message) {
                            "Network Error" -> {
                                _networkFail.value = Unit
                            }
                            else -> {
                                _serverError.value = t.message
                            }
                        }
                    })
            )
        }
    }

    fun onVideoClick(videoUrl: String?) {
        videoUrl?.let {
            _videoUrl.value = videoUrl
        }
    }

}