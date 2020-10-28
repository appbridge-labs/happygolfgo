package com.artilearn.happygolfgo.ui.home.mypage

import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.version.source.VersionRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class MyPageViewModel(
    private val repository: VersionRepository
) : BaseViewModel() {

    init {
        getAppVersion()
    }

    private fun getAppVersion() {
        repository.getAppVersion()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {

            }).addTo(compositeDisposable)
    }

    enum class VersionUpdateModel {
        NECESSARY,
        NEED
    }
}