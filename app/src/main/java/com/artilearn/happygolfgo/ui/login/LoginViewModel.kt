package com.artilearn.happygolfgo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.source.LoginRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel(
    private val loginRepository: LoginRepository
) : BaseViewModel() {

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

    private var test: String? = null

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
            currentPhone, currentPassword, test!!
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
                            _networkError.value = Unit
                        }
                        else -> {
                            _serverError.value = Unit
                        }
                    }
                })
        )
    }


}