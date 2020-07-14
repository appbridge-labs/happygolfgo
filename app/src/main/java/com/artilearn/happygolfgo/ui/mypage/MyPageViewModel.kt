package com.artilearn.happygolfgo.ui.mypage

import android.content.pm.PackageInfo
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.version.source.VersionRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyPageViewModel(
    private val versionRepository: VersionRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val _serverVersion: MutableLiveData<String> = MutableLiveData()
    private val _visiblieUpdate: MutableLiveData<Unit> = MutableLiveData()

    val visiblieUpdate: LiveData<Unit> get() = _visiblieUpdate
    val serverVersion: LiveData<String> get() = _serverVersion

    private var packageInfo: PackageInfo? = null

    init {
        compositeDisposable.add(
            versionRepository.getAppVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _serverVersion.value = it.data.latestVersion
                }, {
                    Log.d(TAG, "fail: $it")
                })
        )
    }

    private fun confirmAppVersion(version: String?) {

    }

}