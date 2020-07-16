package com.artilearn.happygolfgo.ui.pwreset

import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel

class PwResetViewModel : BaseViewModel() {

    val password = MutableLiveData("")
    val passwordConfirm = MutableLiveData("")


}