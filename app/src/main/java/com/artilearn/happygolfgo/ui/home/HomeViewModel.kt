package com.artilearn.happygolfgo.ui.home

import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class HomeViewModel : BaseViewModel() {

    private val bottomNavigation: Subject<BottomNavigationEvent> = PublishSubject.create()
    private val backPressed: Subject<Unit> = PublishSubject.create()

    private val _bottomNavigationButton: SingleLiveEvent<BottomNavigationEvent> = SingleLiveEvent()
    private val _backPressedEvent: SingleLiveEvent<Unit> = SingleLiveEvent()

    val bottomNavigationButton: LiveData<BottomNavigationEvent>
        get() = _bottomNavigationButton

    val backPressedEvent: LiveData<Unit>
        get() = _backPressedEvent

    init {
        rxBind()
    }

    private fun rxBind() {
        bottomNavigation
            .startWith(BottomNavigationEvent.RESERVATION)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _bottomNavigationButton.value = it }
            .addTo(compositeDisposable)

        backPressed.observeOn(AndroidSchedulers.mainThread())
            .subscribe { _backPressedEvent.call() }
            .addTo(compositeDisposable)
    }

    fun bottomNavigation(name: String) {
        when (name) {
            HomeActivity.RESERVATION -> bottomNavigation.onNext(BottomNavigationEvent.RESERVATION)
            HomeActivity.GAME_CENTER -> bottomNavigation.onNext(BottomNavigationEvent.GAME_CENTER)
            HomeActivity.ALRAM -> bottomNavigation.onNext(BottomNavigationEvent.ALRAM)
            HomeActivity.MY_PAGE -> bottomNavigation.onNext(BottomNavigationEvent.MY_PAGE)
        }
    }

    fun backPressed() {
        backPressed.onNext(Unit)
    }

    enum class BottomNavigationEvent {
        RESERVATION,
        GAME_CENTER,
        ALRAM,
        MY_PAGE
    }

}