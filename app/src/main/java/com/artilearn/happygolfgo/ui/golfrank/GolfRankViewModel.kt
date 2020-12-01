package com.artilearn.happygolfgo.ui.golfrank

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.golfrank.GolfRankResponse
import com.artilearn.happygolfgo.data.golfrank.source.GolfRankRepository
import com.artilearn.happygolfgo.ui.golfrank.mapper.GolfRankMapper
import com.artilearn.happygolfgo.ui.golfrank.model.GolfRankModel
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class GolfRankViewModel(
    private val repository: GolfRankRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _trashSubject: PublishSubject<Unit> = PublishSubject.create()
    private val _defaultTrashSubject: BehaviorSubject<Unit> = BehaviorSubject.createDefault(Unit)

    private val mapper: GolfRankMapper = GolfRankMapper
    private val _globalRanks: SingleLiveEvent<List<GolfRankModel>> = SingleLiveEvent()
    private val _localRanks: SingleLiveEvent<List<GolfRankModel>> = SingleLiveEvent()

    val globalRanks: LiveData<List<GolfRankModel>>
        get() = _globalRanks

    val localRanks: LiveData<List<GolfRankModel>>
        get() = _localRanks

    init {
        val trash = _trashSubject
            .startWith(Unit)
            .share()

        val default = Observable.combineLatest(
            trash, _defaultTrashSubject, BiFunction { _: Unit, _: Unit -> Unit }
        ).replay(1).autoConnect()

        val ranks = default
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { showLoading() }
            .switchMapSingle {
                repository.getRanks()
                    .subscribeOn(Schedulers.io())
            }
            .materialize()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { hideLoading() }
            .share()

        ranks.filter { it.isOnNext }
            .map { it.value }
            .map(mapper::globalMapper)
            .subscribe(_globalRanks::setValue)
            .let(compositeDisposable::add)

        ranks.filter { it.isOnNext }
            .map { it.value }
            .map(mapper::localMapper)
            .subscribe(_localRanks::setValue)
            .let(compositeDisposable::add)

        ranks.filter { it.isOnError }
            .subscribe { e ->
                Log.d(TAG, "error: $e, ${e.error?.localizedMessage}")
                val a = e.error
                when(a) {
                    is HttpException -> Log.d(TAG, "error ${a.localizedMessage} ")
                    is NetworkErrorException -> _networkFail.value = Unit
                    else -> Log.d(TAG, "else error: ${a?.localizedMessage}")
                }
            }
            .let(compositeDisposable::add)
    }
}