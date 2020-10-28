package com.artilearn.happygolfgo.ui.home.reservation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.home.HomeResponse
import com.artilearn.happygolfgo.data.home.source.HomeRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import retrofit2.Response
import java.util.concurrent.TimeUnit

class ReservationViewModel(
    private val repository: HomeRepository,
    private val koinContext: Context
) : BaseViewModel() {

    private var disposable: Disposable? = null
    private val reservationList: Subject<Unit> = PublishSubject.create()
    private val _refresh: Subject<Unit> = PublishSubject.create()

    private val _emptyTicket: MutableLiveData<Boolean> = MutableLiveData()
    private val _tickets: MutableLiveData<ArrayList<Ticket>> = MutableLiveData()
    private val _reservationListButton: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _swipeRefresh: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _message = SingleLiveEvent<String>()

    val emptyTicket: LiveData<Boolean>
        get() = _emptyTicket

    val tickets: LiveData<ArrayList<Ticket>>
        get() = _tickets

    val reservationListButton: LiveData<Unit>
        get() = _reservationListButton

    val swipeRefresh: LiveData<Unit>
        get() = _swipeRefresh

    val message: LiveData<String>
        get() = _message

    init {
        bindRx()
        getTicket()
    }

    fun myReservationList() {
        reservationList.onNext(Unit)
    }

    fun ticketRefresh() {
        val ticket = _tickets.value

        if (ticket.isNullOrEmpty()) doNotRefresh()
        else _refresh.onNext(Unit)
    }

    private fun doNotRefresh() {
        _swipeRefresh.call()
        _message.value = koinContext.getString(R.string.fragment_reservation_donot_refresh)
    }

    private fun refreshTicket(): Observable<Response<HomeResponse>> {
        return _refresh.throttleFirst(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { showLoading() }
            .switchMapSingle {
                repository.requestHomeTicket()
                    .subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { hideLoading(); _refresh.onNext(Unit) }
    }

    private fun getTicket() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }

        disposable = repository.requestHomeTicket()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .subscribe({
                val body = it.body()
                if (it.isSuccessful) {
                    if (body != null && body.data.tickets.isNotEmpty()) {
                        hideEmptyTicket()
                        _tickets.value = body.data.tickets as ArrayList<Ticket>
                    } else {
                        showEmptyTicket()
                    }
                } else {
                    _serverError.value = it.message()
                }
            }, { t ->
                when (t.message) {
                    "Network Error" -> _networkFail.value = Unit
                    else -> _serverError.value = t.message
                }
            }).addTo(compositeDisposable)
    }

    private fun bindRx() {
        reservationList.throttleFirst(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _reservationListButton.call() }
            .let(compositeDisposable::add)

        refreshTicket().observeOn(AndroidSchedulers.mainThread())
            .map {

            }
            .subscribe {

            }.addTo(compositeDisposable)
    }

    private fun showEmptyTicket() {
        _emptyTicket.value = true
    }

    private fun hideEmptyTicket() {
        _emptyTicket.value = false
    }
}