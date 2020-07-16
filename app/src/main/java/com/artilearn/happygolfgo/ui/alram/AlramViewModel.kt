package com.artilearn.happygolfgo.ui.alram

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel

class AlramViewModel : BaseViewModel() {

    private val _emptyAlramMessage: MutableLiveData<Boolean> = MutableLiveData()

    val emptyAlramMessage: LiveData<Boolean> get() = _emptyAlramMessage
}