package com.artilearn.happygolfgo.ui.log

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.LogDetail
import com.artilearn.happygolfgo.data.log.source.LogRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class LogViewModel(
    private val logRepository: LogRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _logDetail: SingleLiveEvent<LogDetail> = SingleLiveEvent()
    private val _videoUrl: SingleLiveEvent<String> = SingleLiveEvent()

    val logDetail: LiveData<LogDetail>
        get() = _logDetail

    val videoUrl: LiveData<String>
        get() = _videoUrl

    fun requestLog(lessonId: Int?) {
        lessonId?.let { id ->
            requestLessonLogDetail(id)
        }
    }

    fun onVideoClick(videoUrl: String?) {
        videoUrl?.let {
            _videoUrl.value = videoUrl
        }
    }

    private fun requestLessonLogDetail(lessonId: Int) {
        logRepository.lessonLogDetail(lessonId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t ->
                try {
                    throw RuntimeException(TAG)
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().log("$t\n${t.localizedMessage}")
                }
            }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    _logDetail.value = body.data.lessonLog
                } else {
                    _serverError.value = response.message()
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        try {
                            throw RuntimeException(TAG)
                        } catch (e: Exception) {
                            FirebaseCrashlytics.getInstance().log("$t\n${t.localizedMessage}\n${t.code()}\n${t.message()}")
                        }
                        _serverError.value = t.localizedMessage
                    }
                    else -> _serverError.value = t.localizedMessage
                }
            }).addTo(compositeDisposable)
    }

}