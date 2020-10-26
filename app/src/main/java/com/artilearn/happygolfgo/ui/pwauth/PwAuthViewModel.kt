package com.artilearn.happygolfgo.ui.pwauth

import android.util.Log
import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.pwauth.ConfirmAuthModel
import com.artilearn.happygolfgo.data.pwauth.GetAuthModel
import com.artilearn.happygolfgo.data.pwauth.source.PwAuthRepository
import com.artilearn.happygolfgo.enummodel.PwAuthNumberType
import com.artilearn.happygolfgo.enummodel.PwAuthPhoneType
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlin.math.log

class PwAuthViewModel(
    private val pwAuthRepository: PwAuthRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _phoneNumber = BehaviorSubject.createDefault("")
    private val _authNumber = BehaviorSubject.createDefault("")
    private val _timer = PublishSubject.create<Long>()

    private val _phoneValueType = SingleLiveEvent<PwAuthPhoneType>()
    private val _authValueType = SingleLiveEvent<PwAuthNumberType>()

    val phoneValueType: LiveData<PwAuthPhoneType>
        get() = _phoneValueType

    val authValueType: LiveData<PwAuthNumberType>
        get() = _authValueType

    private var status = false
    private var successPhone = ""
    private var time = 0L

    private val test = Observable.interval(0, 1, TimeUnit.SECONDS)
        .map { seconds ->
            _timer.onNext(seconds)
        }

    init {
        _timer.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it >= 10) {
                    Log.d(TAG, "finish: $it")
                } else {
                    Log.d(TAG, "ing: $it")
                }
            }.addTo(compositeDisposable)

        test.observeOn(AndroidSchedulers.mainThread())
            .subscribe {}
            .addTo(compositeDisposable)
    }

    fun phoneOnNext(str: String) {
        _phoneNumber.onNext(str)
    }

    fun authOnNext(str: String) {
        _authNumber.onNext(str)
    }

    fun btnSendClick() {
        with(_phoneNumber.value) {
            when {
                isNullOrEmpty() -> _phoneValueType.value = PwAuthPhoneType.EMPTY
                this?.length == 11 -> requestAuthNumber(this)
                else -> _phoneValueType.value = PwAuthPhoneType.LEGNTH_FAIL
            }
        }
    }

    fun btnConfirmClick() {
        if (status) {
            confirmAuthNumber()
        } else {
            _authValueType.value = PwAuthNumberType.UNCREATE
        }
    }

    private fun requestAuthNumber(phone: String) {
        val dataModel = GetAuthModel(phone)
        val hashMap = hashMapOf<String, GetAuthModel>()
        hashMap["Data"] = dataModel

        pwAuthRepository.getAuthNumber(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .subscribe({
                _phoneValueType.value = PwAuthPhoneType.SUCCESS
                successPhone = phone
                status = true
            }, {
                when (it.message) {
                    "HTTP 404 Not Found" -> _phoneValueType.value = PwAuthPhoneType.EMPTY_USER
                    "Network Error" -> _phoneValueType.value = PwAuthPhoneType.NETWORK_FAIL
                    else -> _phoneValueType.value = PwAuthPhoneType.FAIL
                }
            }).addTo(compositeDisposable)
    }

    private fun confirmAuthNumber() {
        with(_authNumber.value) {
            when {
                isNullOrEmpty() -> _authValueType.value = PwAuthNumberType.EMPTY
                else -> confirmAuthDataModel(this!!)
            }
        }
    }

    private fun confirmAuthDataModel(authNumber: String) {
        if (successPhone == _phoneNumber.value) {
            val dataModel = ConfirmAuthModel(successPhone, authNumber)
            val hashMap = hashMapOf<String, ConfirmAuthModel>()
            hashMap["Data"] = dataModel

            pwAuthRepository.confirmAuthNumber(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({
                    _authValueType.value = PwAuthNumberType.SUCCESS
                }, {
                    when (it.message) {
                        "Network Error" -> _authValueType.value = PwAuthNumberType.NETWORK_FAIL
                        "HTTP 406 Not Acceptable" -> _authValueType.value = PwAuthNumberType.AUTH_ERROR
                        else -> _authValueType.value = PwAuthNumberType.FAIL
                    }
                }).addTo(compositeDisposable)
        } else {
            _authValueType.value = PwAuthNumberType.MODEL_FAIL
        }
    }

}
