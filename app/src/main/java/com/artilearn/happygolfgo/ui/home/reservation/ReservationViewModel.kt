package com.artilearn.happygolfgo.ui.home.reservation

import android.accounts.NetworkErrorException
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.home.source.HomeRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import kotlin.math.log

class ReservationViewModel(
    private val repository: HomeRepository,
    private val koinContext: Context
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
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

    fun onReservationList() {
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

    private fun refreshTicket(): Observable<Unit> {
        return _refresh.throttleFirst(2500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { showLoading() }
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { hideLoading(); _swipeRefresh.call() }
    }

    //todo 배포 전 주석 해제! (doOnError)
    private fun getTicket() {
        repository.requestHomeTicket()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t ->
                Log.d(TAG, "doOnError: $t")
//                try {
//                    throw RuntimeException(TAG)
//                } catch (e: Exception) {
//                    FirebaseCrashlytics.getInstance().log("""
//                    throwalbe: $e
//                    message: ${e.localizedMessage}
//                """.trimIndent())
//                }
            }
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
                when (t) {
                    is NetworkErrorException -> _networkFail.call()
                    is HttpException -> _serverError.value = t.localizedMessage
                    else -> _serverError.value = "UnKnown Error"
                }
            }).addTo(compositeDisposable)
    }

    private fun bindRx() {
        reservationList.throttleFirst(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _reservationListButton.call() }
            .let(compositeDisposable::add)

        refreshTicket().observeOn(AndroidSchedulers.mainThread())
            .map { _tickets.value?.size }
            .subscribe { ticket ->
                if (ticket != 0) getTicket()
            }.addTo(compositeDisposable)
    }

    private fun showEmptyTicket() {
        _emptyTicket.value = true
    }

    private fun hideEmptyTicket() {
        _emptyTicket.value = false
    }

}