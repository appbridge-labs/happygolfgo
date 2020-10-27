package com.artilearn.happygolfgo.ui.pwreset

import android.util.Log
import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.constants.PASSWORD_REGEX
import com.artilearn.happygolfgo.data.pwreset.source.PwrestRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import java.util.regex.Pattern

class PwResetViewModel(
    private val pwrestRepository: PwrestRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _password = BehaviorSubject.create<String>()
    private val _confirm = BehaviorSubject.create<String>()
    private val _check = Observable.combineLatest(
        passwordValidator(), confirmValidator(), BiFunction { p: Boolean, c: Boolean -> p == c }
    )

    private val _passwordValue = SingleLiveEvent<Boolean>()
    private val _confirmValue = SingleLiveEvent<Boolean>()

    val passwordValue: LiveData<Boolean>
        get() = _passwordValue

    val confirmValue: LiveData<Boolean>
        get() = _confirmValue

    init {
        bindRx()
    }

    fun passwordOnNext(password: String) {
        _password.onNext(password)
    }

    fun confirmOnNext(confirm: String) {
        _confirm.onNext(confirm)
    }

    private fun passwordValidator(): Observable<Boolean> {
        return _password.map { p -> Pattern.matches(PASSWORD_REGEX, p) }
            .doOnNext { _passwordValue.value = it }
    }

    private fun confirmValidator(): Observable<Boolean> {
        return Observable.combineLatest(
            _password, _confirm, BiFunction { p: String, c: String -> p == c }
        )
            .doOnNext { _confirmValue.value = it }
    }

    private fun bindRx() {
        _check.observeOn(AndroidSchedulers.mainThread())
            .subscribe { Log.d(TAG, "bindRx: $it") }
            .addTo(compositeDisposable)
    }

//    val password = MutableLiveData("")
//    val passwordConfirm = MutableLiveData("")
//
//    private val _changeSuccess: MutableLiveData<Unit> = MutableLiveData()
//
//    val changeSuccess: LiveData<Unit> get() = _changeSuccess
//
//    var currentPhone = ""
//    private var currentPassword = ""
//    private var phoneNumber = ""
//
//    init {
//        phoneNumber = pwrestRepository.phoneNumber
//    }
//
//    fun updatePassword() {
//        currentPassword = password.value.toString()
//
//        if (phoneNumber.isEmpty()) {
//            phoneNumber = currentPhone
//        }
//
//        putDataModel(phoneNumber)
//    }
//
//    private fun putDataModel(phone: String) {
//        val dataModel = PwrestModel(phone, currentPassword)
//        val hashMap = hashMapOf<String, PwrestModel>()
//        hashMap["Data"] = dataModel
//
//        compositeDisposable.add(
//            pwrestRepository.changePassword(hashMap)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe { showLoading() }
//                .doAfterTerminate { hideLoading() }
//                .subscribe({
//                    _changeSuccess.value = Unit
//                }, {
//                    when (it.message) {
//                        "Network Error" -> {
//                            _networkFail.value = Unit
//                        }
//                        else -> {
//                            _serverError.value = it.message
//                        }
//                    }
//                })
//        )
//    }

}