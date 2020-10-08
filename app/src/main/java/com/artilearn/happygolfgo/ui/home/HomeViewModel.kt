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

    /**
     * v2 이전
     */
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