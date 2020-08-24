package com.artilearn.happygolfgo.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.developeroption.source.DeveloperOptionRepository

class DeveloperOptionViewModel(
    private val developerOptionRepository: DeveloperOptionRepository
) : BaseViewModel() {

    private val _prodution: MutableLiveData<Unit> = MutableLiveData()
    private val _test: MutableLiveData<Unit> = MutableLiveData()
    private val _developer: MutableLiveData<Unit> = MutableLiveData()

    val prodution: LiveData<Unit> get() = _prodution
    val test: LiveData<Unit> get() = _test
    val developer: LiveData<Unit> get() = _developer

    fun onProdution() {
        developerOptionRepository.environmentMode = "https://happygolfgoapi.com/happygolfgo/appClient/"
        _prodution.value = Unit
    }

    fun onTest() {
        developerOptionRepository.environmentMode = " http://172.30.1.42:7000/appClient/"
        _test.value = Unit
    }

    fun onDeveloper() {
        developerOptionRepository.environmentMode = "http://52.79.253.238/development/appClient/"
        _developer.value = Unit
    }
}