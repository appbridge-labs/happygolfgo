package com.artilearn.happygolfgo.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.developeroption.source.DeveloperOptionRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.Single

class DeveloperOptionViewModel(
    private val developerOptionRepository: DeveloperOptionRepository
) : BaseViewModel() {

    private val _prodution = SingleLiveEvent<String>()
    private val _test = SingleLiveEvent<String>()
    private val _developer = SingleLiveEvent<String>()

    val prodution: LiveData<String>
        get() = _prodution

    val test: LiveData<String>
        get() = _test

    val developer: LiveData<String>
        get() = _developer

    fun onProdution() {
        developerOptionRepository.environmentMode = "https://happygolfgoapi.com/happygolfgo/appClient/"
        _prodution.value = "https://happygolfgoapi.com/happygolfgo/appClient/"
    }

    fun onTest() {
        developerOptionRepository.environmentMode = "http://172.30.1.42:7000/appClient/"
        _test.value = " http://172.30.1.42:7000/appClient/"
    }

    fun onDeveloper() {
        developerOptionRepository.environmentMode = "http://52.79.253.238/development/appClient/"
        _developer.value = "http://52.79.253.238/development/appClient/"
    }
}