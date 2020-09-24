package com.artilearn.happygolfgo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.home.source.HomeRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class HomeViewModel() : BaseViewModel() {

    private val reservation = BehaviorSubject.createDefault(false)
    private val gamecenter = BehaviorSubject.createDefault(false)
    private val alram = BehaviorSubject.createDefault(false)
    private val mypage = BehaviorSubject.createDefault(false)

    private val _bottomReservation = SingleLiveEvent<Boolean>()
    val bottomReservation: LiveData<Boolean>
        get() = _bottomReservation

    private val _bottomGameCenter = SingleLiveEvent<Boolean>()
    val bottomGameCenter: LiveData<Boolean>
        get() = _bottomGameCenter

    private val _bottomAlram = SingleLiveEvent<Boolean>()
    val bottomAlram: LiveData<Boolean>
        get() = _bottomAlram

    private val _bottomMyPage = SingleLiveEvent<Boolean>()
    val bottomMyPage: LiveData<Boolean>
        get() = _bottomMyPage

    init {
        rxBind()
    }

    fun reservationOnNext() {
        with(reservation) {
            if (this.value!!) {
                onNext(false)
            } else {
                onNext(true)
            }
        }
    }

    fun gamecenterOnNext() {
        with(gamecenter) {
            if (this.value!!) {
                onNext(false)
            } else {
                onNext(true)
            }
        }
    }

    fun alramOnNext() {
        with(alram) {
            if (this.value!!) {
                onNext(false)
            } else {
                onNext(true)
            }
        }
    }

    fun mypageOnNext() {
        with(mypage) {
            if (this.value!!) {
                onNext(false)
            } else {
                onNext(true)
            }
        }
    }

    private fun rxBind() {
        reservation.observeOn(AndroidSchedulers.mainThread())
            .skip(1)
            .subscribe { _bottomReservation.value = it }
            .addTo(compositeDisposable)

        gamecenter.observeOn(AndroidSchedulers.mainThread())
            .subscribe { _bottomGameCenter.value = it }
            .addTo(compositeDisposable)

        alram.observeOn(AndroidSchedulers.mainThread())
            .subscribe { _bottomAlram.value = it }
            .addTo(compositeDisposable)

        mypage.observeOn(AndroidSchedulers.mainThread())
            .subscribe { _bottomMyPage.value = it }
            .addTo(compositeDisposable)
    }

//    private val _myPage: MutableLiveData<Unit> = MutableLiveData()
//    private val _alram: MutableLiveData<Unit> = MutableLiveData()
//    private val _emptyTicket: MutableLiveData<Boolean> = MutableLiveData()
//    private val _tickets: MutableLiveData<ArrayList<Ticket>> = MutableLiveData()
//
//    val myPage: LiveData<Unit> get() = _myPage
//    val alram: LiveData<Unit> get() = _alram
//    val emptyTicket: LiveData<Boolean> get() = _emptyTicket
//    val tickets: LiveData<ArrayList<Ticket>> get() = _tickets
//
//    init {
//        requestTicket()
//    }
//
//    fun onMyPageClick() {
//        _myPage.value = Unit
//    }
//
//    fun onAlramClick() {
//        _alram.value = Unit
//    }
//
//    fun requestTicket() {
//        compositeDisposable.add(
//            homeRepository.requestHomeTicket()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe { showLoading() }
//                .doAfterTerminate { hideLoading() }
//                .subscribe({
//                    val body = it.body()
//                    if (it.isSuccessful) {
//                        if (body != null && body.data.tickets.isNotEmpty()) {
//                            hideEmptyTicket()
//                            _tickets.value = body.data.tickets as ArrayList<Ticket>
//                        } else {
//                            showEmptyTicket()
//                        }
//                    } else {
//                        _serverError.value = it.message()
//                    }
//                }, {
//                    when (it.message) {
//                        "Network Error" -> {
//                            _networkFail.value = Unit
//                        }
//                        else -> {
//                            _serverError.value = it.message
//                        }
//                    }
//                })
//        )
//    }
//
//    private fun showEmptyTicket() {
//        _emptyTicket.value = true
//    }
//
//    private fun hideEmptyTicket() {
//        _emptyTicket.value = false
//    }

}