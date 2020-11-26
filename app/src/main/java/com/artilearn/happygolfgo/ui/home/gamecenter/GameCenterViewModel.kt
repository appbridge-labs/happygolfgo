package com.artilearn.happygolfgo.ui.home.gamecenter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.gamecenter.GameCenterResponse
import com.artilearn.happygolfgo.data.gamecenter.source.GameCenterRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.merge
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

class GameCenterViewModel(
    private val repository: GameCenterRepository
) : BaseViewModel() {
    val TAG = javaClass.simpleName
    private val rankDetail: Subject<ButtonType> = PublishSubject.create()
    private val gameDetail: Subject<ButtonType> = PublishSubject.create()
    private val _user: MutableLiveData<GameCenterResponse.GameCenterUser> = MutableLiveData()
    private val _sumary: BehaviorSubject<GameCenterResponse.GameCenterSumary> =
        BehaviorSubject.create()
    private val _averages: BehaviorSubject<GameCenterResponse.GameCenterAverages> =
        BehaviorSubject.create()
    private val _rank: BehaviorSubject<GameCenterResponse.GameCenterRank> = BehaviorSubject.create()

    private val _button: SingleLiveEvent<ButtonType> = SingleLiveEvent()
    val user: LiveData<GameCenterResponse.GameCenterUser>
        get() = _user

    val button: LiveData<ButtonType>
        get() = _button

    init {
        rxBind()

        repository.getGames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            //.doAfterTerminate { hideLoading() }
            .subscribe({
                Log.d(TAG, "$it: ")
                _user.value = it.data.user
            }, {
                Log.d(TAG, "$it: ")
            }).addTo(compositeDisposable)
    }

    private fun rxBind() {
        val rank = rankDetail()
        val game = gameDetail()

        listOf(rank, game)
            .merge()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _button.value = it
            }.addTo(compositeDisposable)
    }

    private fun rankDetail(): Observable<ButtonType> {
        return rankDetail.throttleFirst(1000, TimeUnit.MILLISECONDS)
    }

    private fun gameDetail(): Observable<ButtonType> {
        return gameDetail.throttleFirst(1000, TimeUnit.MILLISECONDS)
    }

    fun onRankDetail() {
        rankDetail.onNext(ButtonType.RANK)
    }

    fun onGameDetail() {
        gameDetail.onNext(ButtonType.GAME)
    }

    enum class ButtonType {
        RANK,
        GAME
    }
}