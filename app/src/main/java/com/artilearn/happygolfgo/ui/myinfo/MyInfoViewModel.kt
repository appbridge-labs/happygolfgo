package com.artilearn.happygolfgo.ui.myinfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import java.util.function.IntUnaryOperator

class MyInfoViewModel : BaseViewModel() {
    private val TAG = javaClass.simpleName

    val nickname = MutableLiveData("")

    private val _success: MutableLiveData<Unit> = MutableLiveData()
    private val _emptyNickName: MutableLiveData<Unit> = MutableLiveData()
    private val _failNickName: MutableLiveData<Unit> = MutableLiveData()

    val success: LiveData<Unit> get() = _success
    val emptyNickName: LiveData<Unit> get() = _emptyNickName
    val failNickName: LiveData<Unit> get() = _failNickName

    private var currentNickName = ""

    fun onConfirm() {
        currentNickName = nickname.value.toString()
        Log.d(TAG, "onConfirm: $currentNickName")

        when {
            currentNickName.isEmpty() -> {
                _emptyNickName.value = Unit
            }
            currentNickName.length < 2 ||
                    currentNickName.length > 8 -> {
                _failNickName.value = Unit
            }
            else -> {
                _success.value = Unit
            }
        }
    }
}