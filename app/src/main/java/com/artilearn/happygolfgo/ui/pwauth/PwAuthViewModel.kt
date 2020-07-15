package com.artilearn.happygolfgo.ui.pwauth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.pwauth.ConfirmAuthModel
import com.artilearn.happygolfgo.data.pwauth.GetAuthModel
import com.artilearn.happygolfgo.data.pwauth.source.PwAuthRepository
import com.artilearn.happygolfgo.enummodel.PwAuthNumberType
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
    private val _authNumber: MutableLiveData<PwAuthNumberType> = MutableLiveData()
    private val _success: MutableLiveData<Boolean> = MutableLiveData()

    val phoneNumber: LiveData<PwAuthPhoneType> get() = _phoneNumber
    val authNumber: LiveData<PwAuthNumberType> get() = _authNumber
    val success: LiveData<Boolean> get() = _success

    private var currentPhone: String = ""
    private var currentAuth: String = ""
    private var status = false

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
        if (status) {
            confirmAuthNumber()
        } else {
            _authNumber.value = PwAuthNumberType.UNCREATE
        }
    }

    private fun requestAuthNumber() {
        val dataModel = GetAuthModel(currentPhone)
        val hashMap = hashMapOf<String, GetAuthModel>()
        hashMap["Data"] = dataModel

        compositeDisposable.add(
            pwAuthRepository.getAuthNumber(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({
                    Log.d(TAG, "requestAuthNumber: success")
                    _phoneNumber.value = PwAuthPhoneType.SUCCESS
                    status = true
                }, {
                    Log.d(TAG, "requestAuthNumber: fail: ${it.message}")
                    when (it.message) {
                        "HTTP 404 Not Found" -> {
                            _phoneNumber.value = PwAuthPhoneType.EMPTY_USER
                        }
                        "Network Error" ->
                            _phoneNumber.value = PwAuthPhoneType.NETWORK_FAIL
                        else -> {
                            _phoneNumber.value = PwAuthPhoneType.FAIL
                        }
                    }
                })
        )
    }

    private fun confirmAuthNumber() {
        currentAuth = auth.value.toString()

        when {
            currentAuth.isEmpty() -> {
                _authNumber.value = PwAuthNumberType.EMPTY
            }
            else -> {
                confirmAuthDataModel()
            }
        }
    }

    private fun confirmAuthDataModel() {
        val dataModel = ConfirmAuthModel(currentPhone, currentAuth)
        val hashMap = hashMapOf<String, ConfirmAuthModel>()
        hashMap["Data"] = dataModel

        compositeDisposable.add(
            pwAuthRepository.confirmAuthNumber(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({
                    Log.d(TAG, "confirmAuthDataModel: success")
                    _authNumber.value = PwAuthNumberType.SUCCESS
                    status = false
                    _success.value = true
                }, {
                    Log.d(TAG, "confirmAuthDataModel: fail: ${it.message}")
                    when (it.message) {
                        "Network Error" -> {
                            _authNumber.value = PwAuthNumberType.NETWORK_FAIL
                        }
                        "HTTP 406 Not Acceptable" -> {
                            _authNumber.value = PwAuthNumberType.AUTH_ERROR
                        }
                        else -> {
                            _authNumber.value = PwAuthNumberType.FAIL
                        }
                    }
                })
        )
    }
}
