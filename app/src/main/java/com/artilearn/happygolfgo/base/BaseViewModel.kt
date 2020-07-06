package com.artilearn.happygolfgo.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> get() = _isLoading

    protected val _networkError: MutableLiveData<Unit> = MutableLiveData()
    val networkError: MutableLiveData<Unit> get() = _networkError

    protected val _serverError: MutableLiveData<Unit> = MutableLiveData()
    val serverError: LiveData<Unit> get() = _serverError

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