package com.artilearn.happygolfgo.ui.golfgame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.golfgame.GolfGameResponse
import com.artilearn.happygolfgo.data.golfgame.source.GolfGameRepository
import com.artilearn.happygolfgo.ui.golfgame.mapper.GolfGameMapper
import com.artilearn.happygolfgo.ui.golfgame.model.GolfGameModel
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class GolfGameViewModel(
    private val repository: GolfGameRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _trashSubject: PublishSubject<Unit> = PublishSubject.create()
    private val _isDefaultTrashSubject: BehaviorSubject<Unit> = BehaviorSubject.createDefault(Unit)

    private val _mapper: GolfGameMapper = GolfGameMapper

    private val _golfGame: SingleLiveEvent<List<GolfGameResponse.DataClass>> = SingleLiveEvent()

    val golfGame: LiveData<List<GolfGameResponse.DataClass>>
        get() = _golfGame

    init {
        val trash = _trashSubject
            .startWith(Unit)
            .share()

        val trashSubject = Observable.combineLatest(
            trash, _isDefaultTrashSubject, BiFunction { _: Unit, _: Unit -> Unit }
        ).replay(1).autoConnect()

        val golfGameData = trashSubject
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { showLoading() }
            .switchMapSingle {
                repository.getGolfGames()
                    .subscribeOn(Schedulers.io())
            }
            .materialize()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { hideLoading() }
            .share()

//        golfGameData.filter { it.isOnNext }
//            .map { it.value }
//            .map { it.data }
//            .map(_mapper::driverMapper)
//            .subscribe {
//                Log.d(TAG, "driver: $it")
//                //_test.value = it
//                //list.add(it)
//                _list.onNext(it)
//            }
//            .let(compositeDisposable::add)
//
//        golfGameData.filter { it.isOnNext }
//            .map { it.value }
//            .map { it.data }
//            .map(_mapper::woodMapper)
//            .subscribe {
//                Log.d(TAG, "wood: $it")
//                //_test.value = it
//                //list.add(it)
//                _list.onNext(it)
//            }
//            .let(compositeDisposable::add)
//
//        golfGameData.filter { it.isOnNext }
//            .map { it.value }
//            .map { it.data }
//            .map(_mapper::ironMapper)
//            .subscribe {
//                Log.d(TAG, "iron: $it")
//                //_test.value = it
//                //list.add(it)
//                _list.onNext(it)
//            }
//            .let(compositeDisposable::add)
//
//        golfGameData.filter { it.isOnNext }
//            .map { it.value }
//            .map { it.data }
//            .map(_mapper::shortMapper)
//            .subscribe {
//                Log.d(TAG, "short: $it")
//                //_test.value = it
//                //list.add(it)
//                _list.onNext(it)
//            }
//            .let(compositeDisposable::add)
//
//        golfGameData.filter { it.isOnNext }
//            .map { it.value }
//            .map { it.data }
//            .map(_mapper::puttMapper)
//            .subscribe {
//                Log.d(TAG, "put: $it")
//                //_test.value = it
//                //list.add(it)
//                _list.onNext(it)
//            }
//            .let(compositeDisposable::add)
//
//        golfGameData.filter { it.isOnNext }
//            .map { it.value }
//            .map { it.data }
//            //.map(_mapper::ironMapper)
//            .subscribe {
//                Log.d(TAG, "all: $it")
//                //_____list.value!!.add(it)
//            }
//            .let(compositeDisposable::add)

        repository.getGolfGames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "sisi: ${it.data}")
            }, {
                Log.d(TAG, "sisi fa: $it")
            }).addTo(compositeDisposable)
    }

}