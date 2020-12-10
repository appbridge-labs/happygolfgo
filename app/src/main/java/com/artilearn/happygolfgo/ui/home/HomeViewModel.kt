package com.artilearn.happygolfgo.ui.home

import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

class HomeViewModel : BaseViewModel() {

    private val bottomNavigation: Subject<BottomNavigationEvent> = PublishSubject.create()
    private val backPressed: Subject<Unit> = PublishSubject.create()
    private val fcmTypeSubject: Subject<String> = BehaviorSubject.create()

    private val _bottomNavigationButton: SingleLiveEvent<BottomNavigationEvent> = SingleLiveEvent()
    private val _backPressedEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _eventType: SingleLiveEvent<FcmTypeMoveModel> = SingleLiveEvent()

    val bottomNavigationButton: LiveData<BottomNavigationEvent>
        get() = _bottomNavigationButton

    val backPressedEvent: LiveData<Unit>
        get() = _backPressedEvent

    val eventType: LiveData<FcmTypeMoveModel>
        get() = _eventType

    init {
        rxBind()

        //todo recordException 없이 log만 쌓아서 안나오는 현상을 발생? 그래서 테스트 중임 확인되면 전체 로그 쌓는데 record 추가할 것
        try {
            throw RuntimeException("error test")
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun fcmOnNext(type: String) {
        fcmTypeSubject.onNext(type)
    }

    private fun rxBind() {
        bottomNavigation
            .distinctUntilChanged()
            .startWith(BottomNavigationEvent.RESERVATION)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _bottomNavigationButton.value = it }
            .addTo(compositeDisposable)

        backPressed.observeOn(AndroidSchedulers.mainThread())
            .subscribe { _backPressedEvent.call() }
            .addTo(compositeDisposable)

        fcmTypeSubject.delay(100, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    "3" -> _eventType.value = FcmTypeMoveModel.PLATE
                    "4" -> _eventType.value = FcmTypeMoveModel.LESSON
                    "6" -> _eventType.value = FcmTypeMoveModel.LOG
                }
            }.addTo(compositeDisposable)
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

    enum class FcmTypeMoveModel {
        PLATE,
        LESSON,
        LOG
    }

}