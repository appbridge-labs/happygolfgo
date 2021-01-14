package com.artilearn.happygolfgo.ui.home.alram

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Alram
import com.artilearn.happygolfgo.data.alram.source.AlramRepository
import com.artilearn.happygolfgo.util.ext.errorLog
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class AlramViewModel(
    private val repository: AlramRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _emptyAlramMessage: MutableLiveData<Boolean> = MutableLiveData()
    private val _alramMessages: MutableLiveData<ArrayList<Alram>> = MutableLiveData()

    val emptyAlramMessage: LiveData<Boolean>
        get() = _emptyAlramMessage

    val alramMessages: LiveData<ArrayList<Alram>>
        get() = _alramMessages

    init {
        requestMessage()
    }

    private fun requestMessage() {
        repository.getAlramMessage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                val body = it.body()
                if (it.isSuccessful && body != null) {
                    if (body.data.notifications.isNotEmpty()) {
                        hideEmptyAlramMessage()
                        _alramMessages.value = body.data.notifications as ArrayList<Alram>
                    } else {
                        showEmptyAlramMessage()
                    }
                } else {
                    _serverError.value = "서버로부터 데이터를 불러오는데 실패했습니다"
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.call()
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    private fun showEmptyAlramMessage() {
        _emptyAlramMessage.value = true
    }

    private fun hideEmptyAlramMessage() {
        _emptyAlramMessage.value = false
    }
}