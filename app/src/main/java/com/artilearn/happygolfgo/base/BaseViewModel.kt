package com.artilearn.happygolfgo.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> get() = _isLoading

    protected val _serverError = SingleLiveEvent<String>()
    val serverError: LiveData<String> get() = _serverError

    protected val _networkFail = SingleLiveEvent<Unit>()
    val networkFail: LiveData<Unit> get() = _networkFail

    protected val _networkDelay: MutableLiveData<Unit> = MutableLiveData()
    val networkDelay: LiveData<Unit> get() = _networkDelay


    protected fun showLoading() {
        _isLoading.value = true
    }

    protected fun hideLoading() {
        _isLoading.value = false
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}