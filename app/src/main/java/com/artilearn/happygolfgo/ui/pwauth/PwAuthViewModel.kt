package com.artilearn.happygolfgo.ui.pwauth

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.pwauth.ConfirmAuthModel
import com.artilearn.happygolfgo.data.pwauth.GetAuthModel
import com.artilearn.happygolfgo.data.pwauth.source.PwAuthRepository
import com.artilearn.happygolfgo.enummodel.PwAuthNumberType
import com.artilearn.happygolfgo.enummodel.PwAuthPhoneType
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class PwAuthViewModel(
    private val pwAuthRepository: PwAuthRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _phoneNumber: MutableLiveData<PwAuthPhoneType> = MutableLiveData()
    private val _authNumber: MutableLiveData<PwAuthNumberType> = MutableLiveData()
    private val _success: MutableLiveData<Boolean> = MutableLiveData()

    val phoneNumber: LiveData<PwAuthPhoneType> get() = _phoneNumber
    val authNumber: LiveData<PwAuthNumberType> get() = _authNumber
    val success: LiveData<Boolean> get() = _success

    var currentPhone: String = ""
    var currentAuth: String = ""
    private var successPhone: String = ""
    private var status = false

    fun btnSendClick() {
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
                .doOnError { t ->
                    when (t.message) {
                        "HTTP 404 Not Found" -> _phoneNumber.value = PwAuthPhoneType.EMPTY_USER
                        "Network Error" -> _phoneNumber.value = PwAuthPhoneType.NETWORK_FAIL
                        else -> _phoneNumber.value = PwAuthPhoneType.FAIL
                    }
                }
                .subscribe({
                    _phoneNumber.value = PwAuthPhoneType.SUCCESS
                    successPhone = currentPhone
                    status = true
                }, { t ->
                    when (t) {
                        is HttpException -> {
                            try {
                                throw RuntimeException(TAG)
                            } catch (e: Exception) {
                                FirebaseCrashlytics.getInstance().log("$t\n${t.localizedMessage}\n${t.code()}\n${t.message()}")
                            }
                            _serverError.value = t.localizedMessage
                        }
                        else -> _serverError.value = t.localizedMessage
                    }
                })
        )
    }

    private fun confirmAuthNumber() {
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
        if (currentPhone == successPhone) {
            val dataModel = ConfirmAuthModel(currentPhone, currentAuth)
            val hashMap = hashMapOf<String, ConfirmAuthModel>()
            hashMap["Data"] = dataModel

            compositeDisposable.add(
                pwAuthRepository.confirmAuthNumber(hashMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showLoading() }
                    .doAfterTerminate { hideLoading() }
                    .doOnError { t ->
                        when(t.message) {
                            "Network Error" -> _authNumber.value = PwAuthNumberType.NETWORK_FAIL
                            "HTTP 406 Not Acceptable" -> _authNumber.value = PwAuthNumberType.AUTH_ERROR
                            else -> _authNumber.value = PwAuthNumberType.FAIL
                        }
                    }
                    .subscribe({
                        _authNumber.value = PwAuthNumberType.SUCCESS
                        _success.value = true
                    }, { t ->
                        when (t) {
                            is HttpException -> {
                                try {
                                    throw RuntimeException(TAG)
                                } catch (e: Exception) {
                                    FirebaseCrashlytics.getInstance().log("$t\n${t.localizedMessage}\n${t.code()}\n${t.message()}")
                                }
                                _serverError.value = t.localizedMessage
                            }
                            else -> _serverError.value = t.localizedMessage
                        }
                    })
            )
        } else {
            _authNumber.value = PwAuthNumberType.MODEL_FAIL
        }
    }
}
