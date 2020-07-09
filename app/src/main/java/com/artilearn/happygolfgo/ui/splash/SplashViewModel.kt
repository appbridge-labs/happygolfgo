package com.artilearn.happygolfgo.ui.splash

import android.util.Log
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.source.SplashRepository
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.PreferenceManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SplashViewModel(
    private val splashRepository: SplashRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val applicationContext = MyApplication.getApplicationContext()

    init {
        getFcmToken()
        selectAccessToken()
    }


    private fun selectAccessToken() {
        val dataModel = SplashTokenModel(splashRepository.clientSecretKey)
        val hashMap = hashMapOf<String, SplashTokenModel>()
        hashMap["Data"] = dataModel

        compositeDisposable.add(
            splashRepository.getAccessToken(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {  }
                .doAfterTerminate {  }
                .subscribe({
                    Log.d(TAG, "selectAccessToken: $it")
                }, {
                    Log.d(TAG, "selectAccessToken: ${it.message}")
                })
        )
    }

    private fun getFcmToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                PreferenceManager(applicationContext).fcmToken = task.result?.token
            })
    }
}