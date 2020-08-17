package com.artilearn.happygolfgo.ui.mypage

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.version.source.VersionRepository
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.PreferenceManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyPageViewModel(
    private val versionRepository: VersionRepository
) : BaseViewModel() {

    private val myApplicationContext = MyApplication.getApplicationContext()

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

    private fun confirmAppVersion(version: String?) {
        version?.let {
            val appVersion = loadAppVersion()

            if (appVersion != version) {
                /* .. */
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