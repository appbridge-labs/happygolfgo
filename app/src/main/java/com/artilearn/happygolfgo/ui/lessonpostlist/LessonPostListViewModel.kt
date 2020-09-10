package com.artilearn.happygolfgo.ui.lessonpostlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.LessonLog
import com.artilearn.happygolfgo.data.lessonlog.source.LessonLogRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LessonPostListViewModel(
    private val lessonLogRepository: LessonLogRepository
) : BaseViewModel() {

    private val _emptyLessonLog: MutableLiveData<Boolean> = MutableLiveData()
    private val _lessonLog: MutableLiveData<ArrayList<LessonLog>> = MutableLiveData()

    val emptyLessonLog: LiveData<Boolean> get() = _emptyLessonLog
    val lessonLog: LiveData<ArrayList<LessonLog>> get() = _lessonLog
    
    init {
        compositeDisposable.add(
            lessonLogRepository.lessonLog()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({
                    val body = it.body()
                    if (it.isSuccessful && body != null) {
                        if (body.data.lessonLogs.isNotEmpty()) {
                            _lessonLog.value = body.data.lessonLogs as ArrayList<LessonLog>
                            hideEmptyLessonLog()
                        } else {
                            showEmptyLessonLog()
                        }
                    } else {
                        _serverError.value = it.message()
                    }
                }, {
                    when (it.message) {
                        "Network Error" -> {
                            _networkFail.value = Unit
                        }
                        else -> {
                            _serverError.value = it.message
                        }
                    }
                })
        )
    }

    private fun showEmptyLessonLog() {
        _emptyLessonLog.value = true
    }

    private fun hideEmptyLessonLog() {
        _emptyLessonLog.value = false
    }
}