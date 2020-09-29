package com.artilearn.happygolfgo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.source.LoginRepository
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.getAndroidID
import com.artilearn.happygolfgo.util.getWidevineID
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val loginRepository: LoginRepository
) : BaseViewModel() {

    private val applicationContext = MyApplication.getApplicationContext()

    val phone = MutableLiveData("")
    val password = MutableLiveData("")
    private var count = 0

    private var currentPhone = ""
    private var currentPassword = ""

    private val _emptyUser: MutableLiveData<Unit> = MutableLiveData()
    private val _failPassword: MutableLiveData<Unit> = MutableLiveData()
    private val _emptyPhone: MutableLiveData<Unit> = MutableLiveData()
    private val _emptyPassword: MutableLiveData<Unit> = MutableLiveData()
    private val _loginSuccess: MutableLiveData<Unit> = MutableLiveData()
    private val _developerOption: MutableLiveData<Unit> = MutableLiveData()
    private val _passwordClick: MutableLiveData<Unit> = MutableLiveData()

    val emptyPassword: LiveData<Unit> get() = _emptyPassword
    val emptyPhone: LiveData<Unit> get() = _emptyPhone
    val failPassword: LiveData<Unit> get() = _failPassword
    val emptyUser: LiveData<Unit> get() = _emptyUser
    val loginSuccess: LiveData<Unit> get() = _loginSuccess
    val developerOption: LiveData<Unit> get() = _developerOption
    val passwordClick: LiveData<Unit> get() = _passwordClick

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
        _passwordClick.value = Unit
    }

    fun onDeveloperOption() {
        count++
        if (count >= 10) {
            count = 0
            _developerOption.value = Unit
        }
    }

    private fun requestLogin() {
        val id = getAndroidID(applicationContext)
        val uuid = getWidevineID()
        val dataModel = LoginDataModel(
            currentPhone, currentPassword, loginRepository.fcmToken, uuid, id
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