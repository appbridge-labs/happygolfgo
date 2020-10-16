package com.artilearn.happygolfgo.ui.mypage

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.version.source.VersionRepository
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MyPageViewModel(
    private val versionRepository: VersionRepository
) : BaseViewModel() {

    private val myApplicationContext = MyApplication.getApplicationContext()
    private val updateClick = PublishSubject.create<Unit>()

    private var packageInfo: PackageInfo? = null

    private val _logout = SingleLiveEvent<Unit>()
    private val _fail = SingleLiveEvent<String>()
    private val _versionText = SingleLiveEvent<String>()
    private val _update = SingleLiveEvent<UpdateModel>()
    private val _playStore = SingleLiveEvent<Unit>()

    val logout: LiveData<Unit> get() = _logout
    val fail: LiveData<String> get() = _fail
    val versionText: LiveData<String> get() = _versionText
    val update: LiveData<UpdateModel> get() = _update
    val playStore: LiveData<Unit> get() = _playStore

    init {
        compositeDisposable.add(
            versionRepository.getAppVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _versionText.value = loadAppVersion()
                    confirmAppVersion(it.data.latestVersion)
                }, {
                    _versionText.value = loadAppVersion()
                })
        )

        bindRx()
    }

    private fun confirmAppVersion(version: String?) {
        version?.let {
            val server = it.substring(2).toFloat()
            val local = loadAppVersion()?.substring(2)?.toFloat()

            if (local == null) {
                _update.value = UpdateModel.NECESSARY
            } else {
                when {
                    local < server -> _update.value = UpdateModel.NEED
                    local >= server -> _update.value = UpdateModel.NECESSARY
                    else -> _update.value = UpdateModel.NECESSARY
                }
            }
        }
    }

    private fun loadAppVersion(): String? {
        try {
            packageInfo = myApplicationContext.packageManager
                .getPackageInfo(myApplicationContext.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        val versionCode = packageInfo?.versionCode

        return packageInfo?.versionName
    }

    fun onUpdateClick() {
        updateClick.onNext(Unit)
    }

    fun userLogout() {
        versionRepository.userLogout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .subscribe({
                _logout.call()
            }, {
                _fail.value = it.message
            }).let(compositeDisposable::add)
    }

    private fun bindRx() {
        updateClick.throttleFirst(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _playStore.call() }
            .addTo(compositeDisposable)
    }

    enum class UpdateModel {
        NECESSARY,
        NEED
    }

}