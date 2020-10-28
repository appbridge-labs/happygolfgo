package com.artilearn.happygolfgo.ui.home.alram

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Alram
import com.artilearn.happygolfgo.data.alram.source.AlramRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class AlramViewModel(
    private val repository: AlramRepository
) : BaseViewModel() {

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
                    _serverError.value = it.message()
                }
            }, {
                when (it.message) {
                    "Network Error" -> _networkFail.value = Unit
                    else -> _serverError.value = it.message
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