package com.artilearn.happygolfgo.ui.home.gamecenter

import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.merge
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

class GameCenterViewModel : BaseViewModel() {

    private val rankDetail: Subject<ButtonType> = PublishSubject.create()
    private val gameDetail: Subject<ButtonType> = PublishSubject.create()

    private val _button: SingleLiveEvent<ButtonType> = SingleLiveEvent()

    val button: LiveData<ButtonType> get() = _button

    init {
        rxBind()
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