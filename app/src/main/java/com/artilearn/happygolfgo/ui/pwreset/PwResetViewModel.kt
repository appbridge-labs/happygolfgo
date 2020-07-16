package com.artilearn.happygolfgo.ui.pwreset

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.pwreset.PwrestModel
import com.artilearn.happygolfgo.data.pwreset.source.PwrestRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PwResetViewModel(
    private val pwrestRepository: PwrestRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    val password = MutableLiveData("")
    val passwordConfirm = MutableLiveData("")

    private var currentPassword = ""
    private var phoneNumber = ""

    init {
        phoneNumber = pwrestRepository.phoneNumber
    }

    fun updatePassword() {
        currentPassword = password.value.toString()
        Log.d(TAG, "updatePassword: $currentPassword")

        val dataModel = PwrestModel(phoneNumber, currentPassword)
        val hashMap = hashMapOf<String, PwrestModel>()
        hashMap["Data"] = dataModel
        
        compositeDisposable.add(
            pwrestRepository.changePassword(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({
                    Log.d(TAG, "updatePassword: success")
                }, {
                    Log.d(TAG, "updatePassword: ${it.message}")
                })
        )
    }

}