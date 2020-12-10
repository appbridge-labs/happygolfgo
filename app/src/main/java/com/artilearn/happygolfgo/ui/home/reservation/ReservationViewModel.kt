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
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

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
    private val _buttonState: MutableLiveData<Boolean> = MutableLiveData(true)

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

    val buttonState: LiveData<Boolean>
        get() = _buttonState

    init {
        bindRx()
        getTicket()

        //todo recordException 없이 log만 쌓아서 안나오는 현상을 발생? 그래서 테스트 중임 확인되면 전체 로그 쌓는데 record 추가할 것?? 좀더 확인
        //todo (2020.12.10) 에러 리포트 파이어베이스 확인 하면 전체 로그쌓는데 확인 후 수정 후 배포해야함  -> record + log로 하기
        try {
            throw RuntimeException("$TAG error both")
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            FirebaseCrashlytics.getInstance().log("error both test")
        }
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
//                    throw RuntimeException(t)
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

    fun onButtonState(result: Boolean) {
        _buttonState.value = result
    }

}