package com.artilearn.happygolfgo.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.source.LoginRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel(
    private val loginRepository: LoginRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    val phone = MutableLiveData("")
    val password = MutableLiveData("")

    private var currentPhone = ""
    private var currentPassword = ""

    private val _emptyUser: MutableLiveData<Unit> = MutableLiveData()
    private val _failPassword: MutableLiveData<Unit> = MutableLiveData()
    private val _emptyPhone: MutableLiveData<Unit> = MutableLiveData()
    private val _emptyPassword: MutableLiveData<Unit> = MutableLiveData()
    private val _loginSuccess: MutableLiveData<Unit> = MutableLiveData()

    val emptyPassword: LiveData<Unit> get() = _emptyPassword
    val emptyPhone: LiveData<Unit> get() = _emptyPhone
    val failPassword: LiveData<Unit> get() = _failPassword
    val emptyUser: LiveData<Unit> get() = _emptyUser
    val loginSuccess: LiveData<Unit> get() = _loginSuccess

    fun onLoginClick() {
        currentPhone = phone.value.toString()
        currentPassword = password.value.toString()

        when {
            currentPhone.isEmpty() -> {
                _emptyPhone.value = Unit
            }
            currentPassword.isEmpty() -> {
                _emptyPassword.value = Unit
            }
            else -> {
                requestLogin()
            }
        }

    }

    fun onPasswordForgetClick() {

    }

    private fun requestLogin() {
        val dataModel = LoginDataModel(
            currentPhone, currentPassword, loginRepository.fcmToken
        )

        val hashMap = hashMapOf<String, LoginDataModel>()
        hashMap["Data"] = dataModel

        compositeDisposable.add(
            loginRepository.repositoryLogin(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({
                    _loginSuccess.value = Unit
                }, {
                    when (it.message) {
                        "HTTP 401 Unauthorized", "HTTP 401 " -> {
                            _failPassword.value = Unit
                        }
                        "HTTP 404 Not Found", "HTTP 404 " -> {
                            _emptyUser.value = Unit
                        }
                        "Network Error" -> {
                            _networkFail.value = Unit
                        }
                        else -> {
                            _serverError.value = it.message
                        }
                    }
                })
        )
    }


}