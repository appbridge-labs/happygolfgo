package com.artilearn.happygolfgo.ui.pwreset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.pwreset.PwrestModel
import com.artilearn.happygolfgo.data.pwreset.source.PwrestRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PwResetViewModel(
    private val pwrestRepository: PwrestRepository
) : BaseViewModel() {

    val password = MutableLiveData("")
    val passwordConfirm = MutableLiveData("")

    private val _changeSuccess: MutableLiveData<Unit> = MutableLiveData()

    val changeSuccess: LiveData<Unit> get() = _changeSuccess

    var currentPhone = ""
    private var currentPassword = ""
    private var phoneNumber = ""

    init {
        phoneNumber = pwrestRepository.phoneNumber
    }

    fun updatePassword() {
        currentPassword = password.value.toString()

        if (phoneNumber.isEmpty()) {
            phoneNumber = currentPhone
        }

        putDataModel(phoneNumber)
    }

    private fun putDataModel(phone: String) {
        val dataModel = PwrestModel(phone, currentPassword)
        val hashMap = hashMapOf<String, PwrestModel>()
        hashMap["Data"] = dataModel

        compositeDisposable.add(
            pwrestRepository.changePassword(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({
                    _changeSuccess.value = Unit
                }, {
                    when (it.message) {
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