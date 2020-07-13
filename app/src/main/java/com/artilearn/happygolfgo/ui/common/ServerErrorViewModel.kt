package com.artilearn.happygolfgo.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel

class ServerErrorViewModel : BaseViewModel() {

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()

    val errorMessage: LiveData<String> get() = _errorMessage
}