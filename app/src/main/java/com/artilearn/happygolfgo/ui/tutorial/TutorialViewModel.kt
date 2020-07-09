package com.artilearn.happygolfgo.ui.tutorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.tutorial.source.TutorialRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class TutorialViewModel(
    private val tutorialRepository: TutorialRepository
) : BaseViewModel() {

    private val _btnAnimation: MutableLiveData<Unit> = MutableLiveData()
    private val _goLogin: MutableLiveData<Unit> = MutableLiveData()

    val btnAnimation: LiveData<Unit> get() = _btnAnimation
    val goLogin: LiveData<Unit> get() = _goLogin

    init {
        compositeDisposable.add(
            Completable.create { emitter ->
                emitter.onComplete()
            }
                .delay(1500L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _btnAnimation.value = Unit
                }
        )
    }

    fun onStartClick() {
        tutorialRepository.tutorial = true
        _goLogin.value = Unit
    }
}