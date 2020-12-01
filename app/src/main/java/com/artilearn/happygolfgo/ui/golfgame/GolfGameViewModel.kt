package com.artilearn.happygolfgo.ui.golfgame

import android.util.Log
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.golfgame.source.GolfGameRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GolfGameViewModel(
    private val repository: GolfGameRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    init {
        repository.getGolfGames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "s: ${it.data.driver}\n${it.data.woodUtil}\n${it.data.iron}\n${it.data.shortGame}\n${it.data.putter}")
            }, {
                Log.d(TAG, "f: $it")
            }).let(compositeDisposable::add)
    }

}