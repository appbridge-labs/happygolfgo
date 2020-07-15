package com.artilearn.happygolfgo.ui.pwauth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.enummodel.PwAuthPhoneType

class PwAuthViewModel : BaseViewModel() {
    private val TAG = javaClass.simpleName

    val phone = MutableLiveData("")
    val auth = MutableLiveData("")

    private val _phoneNumber: MutableLiveData<PwAuthPhoneType> = MutableLiveData()

    val phoneNumber: LiveData<PwAuthPhoneType> get() = _phoneNumber

    private var currentPhone: String = ""
    private var currentAuth: String = ""

    fun btnSendClick() {
        currentPhone = phone.value.toString()

        when {
            currentPhone.isEmpty() -> {
                _phoneNumber.value = PwAuthPhoneType.EMPTY
            }
            currentPhone.length == 11 -> {
                _phoneNumber.value = PwAuthPhoneType.SUCCESS
            }
            else -> {
                _phoneNumber.value = PwAuthPhoneType.FAIL
            }
        }
    }

    fun btnConfirmClick() {

    }
}
