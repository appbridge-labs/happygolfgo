package com.artilearn.happygolfgo.ui.common

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class NetworkFailViewModel : BaseViewModel() {

    private val _btnRetry: MutableLiveData<Unit> = MutableLiveData()

    val btnRetry: LiveData<Unit> get() = _btnRetry

    fun onRetryClick() {
        showLoading()
        compositeDisposable.add(
            Completable.create { emitter ->
                emitter.onComplete()
            }
                .delay(1500L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _btnRetry.value = Unit
                    hideLoading()
                }
        )
    }
}