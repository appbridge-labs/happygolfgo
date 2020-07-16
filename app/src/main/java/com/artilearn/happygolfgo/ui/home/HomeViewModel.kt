package com.artilearn.happygolfgo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.home.source.HomeRepository

class HomeViewModel(
    private val homeRepository: HomeRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val _myPage: MutableLiveData<Unit> = MutableLiveData()
    private val _alram: MutableLiveData<Unit> = MutableLiveData()

    val myPage: LiveData<Unit> get() = _myPage
    val alram: LiveData<Unit> get() = _alram

    fun onMyPageClick() {
        _myPage.value = Unit
    }

    fun onAlramClick() {
        _alram.value = Unit
    }

}