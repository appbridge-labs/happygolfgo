package com.artilearn.happygolfgo.ui.home.reservation

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.home.source.HomeRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class ReservationViewModel(
    private val repository: HomeRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val reservationList: Subject<Unit> = PublishSubject.create()
    private val _isRefreshSubject: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    private val _onRefreshSubject: PublishSubject<Unit> = PublishSubject.create()
    private var disposable: Disposable? = null

    private val _emptyTicket: MutableLiveData<Boolean> = MutableLiveData()
    private val _tickets: MutableLiveData<ArrayList<Ticket>> = MutableLiveData()
    private val _reservationListButton: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _message = SingleLiveEvent<String>()
    private val _buttonState: MutableLiveData<Boolean> = MutableLiveData(true)
    private val _isRefresh: MutableLiveData<Boolean> = MutableLiveData()
    val isRefresh: LiveData<Boolean>
        get() = _isRefresh

    val emptyTicket: LiveData<Boolean>
        get() = _emptyTicket

    val tickets: LiveData<ArrayList<Ticket>>
        get() = _tickets

    val reservationListButton: LiveData<Unit>
        get() = _reservationListButton

    val message: LiveData<String>
        get() = _message

    val buttonState: LiveData<Boolean>
        get() = _buttonState

    init {
        compositeDisposable.addAll(
            reservationList.throttleFirst(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _reservationListButton.call() },

            _onRefreshSubject.observeOn(AndroidSchedulers.mainThread())
                .doOnNext { _isRefreshSubject.onNext(true) }
                .map { _tickets.value?.size }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { _isRefreshSubject.onNext(false) }
                .subscribe { it?.let { getTicket() } },

            _isRefreshSubject.observeOn(AndroidSchedulers.mainThread())
                .subscribe(_isRefresh::setValue)
        )
    }

    fun onReservationList() {
        reservationList.onNext(Unit)
    }

    fun getTicket() {
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
            .doOnError { t -> errorLog(TAG, t) }
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
                    _serverError.value = "서버로부터 데이터를 불러오는데 실패했습니다"
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.call()
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    private fun showEmptyTicket() {
        _emptyTicket.value = true
    }

    private fun hideEmptyTicket() {
        _emptyTicket.value = false
    }

    fun onRefresh() = _onRefreshSubject.onNext(Unit)

    fun onButtonState(result: Boolean) {
        _buttonState.value = result
    }

}