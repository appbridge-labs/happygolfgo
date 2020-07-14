package com.artilearn.happygolfgo.ui.mypage

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.version.source.VersionRepository
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.PreferenceManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyPageViewModel(
    private val versionRepository: VersionRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val myApplicationContext = MyApplication.getApplicationContext()

    private val _showUpdate: MutableLiveData<Unit> = MutableLiveData()
    private val _goPlayStore: MutableLiveData<Unit> = MutableLiveData()

    val showUpdate: LiveData<Unit> get() = _showUpdate
    val goPlayStore: LiveData<Unit> get() = _goPlayStore

    private var packageInfo: PackageInfo? = null

    init {
        compositeDisposable.add(
            versionRepository.getAppVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    confirmAppVersion(it.data.latestVersion)
                }, {
                    confirmAppVersion(PreferenceManager(myApplicationContext).appVersion)
                })
        )
    }

    fun onUpdateClick() {
        _goPlayStore.value = Unit
    }

    private fun confirmAppVersion(version: String?) {
        version?.let {
            val appVersion = loadAppVersion()

            if (appVersion != version) {
                _showUpdate.value = Unit
            }
        }
    }

    fun loadAppVersion(): String? {
        try {
            packageInfo = myApplicationContext.packageManager
                .getPackageInfo(myApplicationContext.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        val versionCode = packageInfo?.versionCode

        return packageInfo?.versionName
    }

}