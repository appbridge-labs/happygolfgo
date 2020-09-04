package com.artilearn.happygolfgo.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SynchronizedDataModel
import com.artilearn.happygolfgo.data.splash.source.SplashRepository
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.PreferenceManager
import com.artilearn.happygolfgo.util.notificationCheck
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashViewModel(
    private val splashRepository: SplashRepository
) : BaseViewModel() {

    private val applicationContext = MyApplication.getApplicationContext()

    private val _goHome: MutableLiveData<Unit> = MutableLiveData()
    private val _goLogin: MutableLiveData<Unit> = MutableLiveData()
    private val _goTutorial: MutableLiveData<Unit> = MutableLiveData()

    val goHome: LiveData<Unit> get() = _goHome
    val goLogin: LiveData<Unit> get() = _goLogin
    val goTutorial: LiveData<Unit> get() = _goTutorial

    init {
        getFcmToken()
        splash()
    }

    private fun splash() {
        Completable.create { emitter ->
            emitter.onComplete()
        }
            .delay(1200L, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                splashStatus()
            }.addTo(compositeDisposable)
    }

    private fun splashStatus() {
        if (splashRepository.tutorial) {
            if (splashRepository.autoLogin) {
                confirmClientSecretKey()
            } else {
                _goLogin.value = Unit
            }
        } else {
            _goTutorial.value = Unit
        }
    }

    private fun confirmClientSecretKey() {
        if (splashRepository.clientSecretKey.isNotEmpty()) {
            selectAccessToken()
        } else {
            _goLogin.value = Unit
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
                .doOnSubscribe { }
                .doAfterTerminate { }
                .subscribe({
                    userSynchronized()
                }, { t ->
                    _goHome.value = Unit
                })
        )
    }

    private fun userSynchronized() {
        val userPushCheck = notificationCheck(applicationContext)

        val dataModel = SynchronizedDataModel(
            splashRepository.clientSecretKey,
            splashRepository.fcmToken ?: "",
            userPushCheck
        )

        val hashMap = hashMapOf<String, SynchronizedDataModel>()
        hashMap["Data"] = dataModel

        compositeDisposable.add(
            splashRepository.userSynchronized(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { }
                .doAfterTerminate { }
                .subscribe({
                    _goHome.value = Unit
                }, { t ->
                    _goHome.value = Unit
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