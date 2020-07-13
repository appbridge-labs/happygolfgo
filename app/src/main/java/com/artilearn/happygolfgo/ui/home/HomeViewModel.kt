package com.artilearn.happygolfgo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel

class HomeViewModel : BaseViewModel() {

    private val _ticketEmpty: MutableLiveData<Unit> = MutableLiveData()

    val ticketEmpty: LiveData<Unit> get() = _ticketEmpty
}