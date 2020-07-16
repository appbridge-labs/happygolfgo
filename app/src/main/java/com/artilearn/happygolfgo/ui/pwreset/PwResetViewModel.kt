package com.artilearn.happygolfgo.ui.pwreset

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class PwResetViewModel : BaseViewModel() {
    private val TAG = javaClass.simpleName

    val password = MutableLiveData("")
    val passwordConfirm = MutableLiveData("")

    fun updatePassword() {

    }

}