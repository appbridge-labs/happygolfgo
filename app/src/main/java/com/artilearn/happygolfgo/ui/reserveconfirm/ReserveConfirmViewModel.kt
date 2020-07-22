package com.artilearn.happygolfgo.ui.reserveconfirm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.reserveconfirm.source.ReserveConfirmRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ReserveConfirmViewModel(
    private val reserveConfirmRepository: ReserveConfirmRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val _policy: MutableLiveData<String> = MutableLiveData()

    val policy: LiveData<String> get() = _policy

    private var currentPolicy = ""
    
    fun requestPolicy() {
        compositeDisposable.add(
            reserveConfirmRepository.requestPolicy()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({ response ->
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        Log.d(TAG, "${body.data.policy.title}\n${body.data.policy.body} ")
                        _policy.value = body.data.policy.body
                    } else {
                        _serverError.value = response.errorBody().toString()
                    }
                }, { t ->
                    Log.d(TAG, "fail: ${t.message}")
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