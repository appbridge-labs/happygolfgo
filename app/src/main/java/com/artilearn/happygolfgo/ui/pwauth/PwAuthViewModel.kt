package com.artilearn.happygolfgo.ui.pwauth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.pwauth.GetAuthModel
import com.artilearn.happygolfgo.data.pwauth.source.PwAuthRepository
import com.artilearn.happygolfgo.enummodel.PwAuthPhoneType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PwAuthViewModel(
    private val pwAuthRepository: PwAuthRepository
) : BaseViewModel() {
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
                requestAuthNumber()
            }
            else -> {
                _phoneNumber.value = PwAuthPhoneType.LEGNTH_FAIL
            }
        }
    }

    fun btnConfirmClick() {

    }

    private fun requestAuthNumber() {
        val dataModel = GetAuthModel(currentPhone)
        val hashMap = hashMapOf<String, GetAuthModel>()
        hashMap["Data"] = dataModel

        compositeDisposable.add(
            pwAuthRepository.sendAuthNumber(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({
                    Log.d(TAG, "requestAuthNumber: success")
                    _phoneNumber.value = PwAuthPhoneType.SUCCESS
                }, {
                    Log.d(TAG, "requestAuthNumber: fail: ${it.message}")
                    when (it.message) {
                        "HTTP 404 Not Found" -> {
                            _phoneNumber.value = PwAuthPhoneType.EMPTY_USER
                        }
                        else -> {
                            _phoneNumber.value = PwAuthPhoneType.FAIL
                        }
                    }
                })
        )
    }
}
