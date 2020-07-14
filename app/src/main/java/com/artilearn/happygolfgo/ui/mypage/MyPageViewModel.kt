package com.artilearn.happygolfgo.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel

class MyPageViewModel : BaseViewModel() {

    private val _visiblieUpdate: MutableLiveData<Unit> = MutableLiveData()

    val visiblieUpdate: LiveData<Unit> get() = _visiblieUpdate


}