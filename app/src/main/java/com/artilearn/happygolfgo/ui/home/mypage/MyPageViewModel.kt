package com.artilearn.happygolfgo.ui.home.mypage

import android.accounts.NetworkErrorException
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.version.LogoutModel
import com.artilearn.happygolfgo.data.version.source.VersionRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import com.artilearn.happygolfgo.util.getWidevineID
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class MyPageViewModel(
    private val repository: VersionRepository,
    private val koinContext: Context
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _appUpdate: Subject<Unit> = PublishSubject.create()

    private val _eventType: SingleLiveEvent<MyPageStateType> = SingleLiveEvent()
    private val _message = SingleLiveEvent<String>()
    private val _appVersion = SingleLiveEvent<String>()
    private val _text: MutableLiveData<String> = MutableLiveData()
    private val _image: MutableLiveData<String> = MutableLiveData()

    val message: LiveData<String>
        get() = _message

    val eventType: LiveData<MyPageStateType>
        get() = _eventType

    val appVersion: LiveData<String>
        get() = _appVersion

    val text: LiveData<String>
        get() = _text

    val image: LiveData<String>
        get() = _image

    private var packageInfo: PackageInfo? = null

    init {
        bindRx()
        getAppVersion()
        initSetBinding()
    }

    fun appUpdateOnNext() {
        _appUpdate.onNext(Unit)
    }

    fun logout() {
        val logoutModel = LogoutModel(identifier = getWidevineID())
        val data = hashMapOf<String, LogoutModel>()
        data["Data"] = logoutModel

        repository.userLogout(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                _eventType.value = MyPageStateType.LOGOUT
            }, { e ->
                when (e) {
                    is NetworkErrorException -> _networkFail.call()
                    is HttpException -> {
                        if (e.code() == 419) _appRefresh.call()
                        else _message.value = "로그아웃 불가"
                    }
                    else -> _message.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    private fun initSetBinding() {
        val pair = Pair(repository.name, " (${repository.nickName})")
        _text.value = pair.first + pair.second

        _image.value = repository.profileImage
    }

    private fun getAppVersion() {
        repository.getAppVersion()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                _appVersion.value = loadCurrentAppVersion()
                compareAppVersion(it.data.latestVersion)
            }, {
                _appVersion.value = loadCurrentAppVersion()
            }).addTo(compositeDisposable)
    }

    private fun compareAppVersion(version: String?) {
        version?.let {
            val server = it.substring(2).toFloat()
            val local = loadCurrentAppVersion()?.substring(2)?.toFloat()

            if (local == null) {
                _eventType.value = MyPageStateType.NECESSARY_UPDATE
            } else {
                when {
                    local < server -> _eventType.value = MyPageStateType.NEED_UPDATE
                    local >= server -> _eventType.value = MyPageStateType.NECESSARY_UPDATE
                    else -> _eventType.value = MyPageStateType.NECESSARY_UPDATE
                }
            }
        }
    }

    private fun loadCurrentAppVersion(): String? {
        try {
            packageInfo = koinContext.packageManager
                .getPackageInfo(koinContext.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return packageInfo?.versionName
    }

    private fun bindRx() {
        _appUpdate.throttleFirst(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _eventType.value = MyPageStateType.PLAY_STORE }
            .addTo(compositeDisposable)
    }

    enum class MyPageStateType {
        NECESSARY_UPDATE,
        NEED_UPDATE,
        LOGOUT,
        PLAY_STORE
    }

}