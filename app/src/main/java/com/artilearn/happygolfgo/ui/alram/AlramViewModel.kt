package com.artilearn.happygolfgo.ui.alram

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.alram.source.AlramRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AlramViewModel(
    private val alramRepository: AlramRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val _emptyAlramMessage: MutableLiveData<Boolean> = MutableLiveData()

    val emptyAlramMessage: LiveData<Boolean> get() = _emptyAlramMessage

    init {
        compositeDisposable.add(
            alramRepository.getAlramMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({
                    Log.d(TAG, "success: $it")
                    val body = it.body()
                    if (it.isSuccessful && body != null) {
                        if (body.data.notifications.isNotEmpty()) {
                            hideEmptyAlramMessage()
                        } else {
                            showEmptyAlramMessage()
                        }
                    } else {
                        _serverError.value = it.message()
                    }
                }, {
                    Log.d(TAG, "fail: ${it.message}")
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

    private fun showEmptyAlramMessage() {
        _emptyAlramMessage.value = true
    }

    private fun hideEmptyAlramMessage() {
        _emptyAlramMessage.value = false
    }
}