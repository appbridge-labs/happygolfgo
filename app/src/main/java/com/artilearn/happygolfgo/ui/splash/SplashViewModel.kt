package com.artilearn.happygolfgo.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.source.SplashRepository
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.PreferenceManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashViewModel(
    private val splashRepository: SplashRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val applicationContext = MyApplication.getApplicationContext()

    private val _goMain: MutableLiveData<Unit> = MutableLiveData()
    private val _goLogin: MutableLiveData<Unit> = MutableLiveData()
    private val _goTutorial: MutableLiveData<Unit> = MutableLiveData()

    val goMain: LiveData<Unit> get() = _goMain
    val goLogin: LiveData<Unit> get() = _goLogin
    val goTutorial: LiveData<Unit> get() = _goTutorial

    init {
        getFcmToken()
        Log.d(TAG, "key: ${PreferenceManager(applicationContext).clientScreetKey}")

        compositeDisposable.add(
            Completable.create { emitter ->
                emitter.onComplete()
            }
                .delay(1200L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    userStatusConfirm()
                }
        )
    }

    private fun userStatusConfirm() {
        Log.d(TAG, "userStatusConfirm: ${splashRepository.autoLogin}\n${splashRepository.tutorial}")
        if (splashRepository.tutorial) {
            if (splashRepository.autoLogin) {
                selectAccessToken()
            } else {
                _goLogin.value = Unit
            }
        } else {
            _goTutorial.value = Unit
        }
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