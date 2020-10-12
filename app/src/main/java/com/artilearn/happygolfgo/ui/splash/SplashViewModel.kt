package com.artilearn.happygolfgo.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SynchronizedDataModel
import com.artilearn.happygolfgo.data.splash.source.SplashRepository
import com.artilearn.happygolfgo.util.*
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
    private val _needUpdate = SingleLiveEvent<String>()

    val goHome: LiveData<Unit> get() = _goHome
    val goLogin: LiveData<Unit> get() = _goLogin
    val goTutorial: LiveData<Unit> get() = _goTutorial
    val needUpdate: LiveData<String> get() = _needUpdate

    init {
        val token = splashRepository.fcmToken
        if (token.isNullOrEmpty()) {
            getFcmToken()
        }
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
            userSynchronized()
        } else {
            _goLogin.value = Unit
        }
    }

    private fun selectAccessToken() {
        val uuid = getWidevineID()
        val dataModel = SplashTokenModel(splashRepository.clientSecretKey, uuid)
        val hashMap = hashMapOf<String, SplashTokenModel>()
        hashMap["Data"] = dataModel

        compositeDisposable.add(
            splashRepository.getAccessToken(hashMap)
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

    private fun userSynchronized() {
        val userPushCheck = notificationCheck(applicationContext)
        val id = getAndroidID(applicationContext)
        val uuid = getWidevineID()

        val dataModel = SynchronizedDataModel(
            splashRepository.clientSecretKey,
            splashRepository.fcmToken ?: "",
            userPushCheck,
            uuid,
            id
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
                    if (it.code() == 200) {
                        selectAccessToken()
                    } else {
                        deleteUser()
                    }
                }, { t ->
                    selectAccessToken()
                })
        )
    }

    private fun deleteUser() {
        splashRepository.deleteUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { }
            .doAfterTerminate { }
            .subscribe({
                _needUpdate.value = "최신 업데이트와 보안상의 이유로 다시 한번 로그인 부탁드립니다"
            }, { t ->
                _goLogin.value = Unit
            }).addTo(compositeDisposable)
    }

    private fun getFcmToken() {
        splashRepository.getFcmToken()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {})
            .addTo(compositeDisposable)
    }

}