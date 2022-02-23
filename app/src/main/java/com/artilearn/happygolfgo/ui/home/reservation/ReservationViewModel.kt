package com.artilearn.happygolfgo.ui.home.reservation

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.home.HomeResponse
import com.artilearn.happygolfgo.data.home.HomeResponseInfo
import com.artilearn.happygolfgo.data.home.source.HomeRepository
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.EXPIRED_TICKET
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.PAUSE_TICKET
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.USE_TICKET
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

//comments: Eddie Kim, 홈화면에서 티켓에 대한 정보를 가져오는 부분
class ReservationViewModel(
    private val repository: HomeRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val reservationList: Subject<Unit> = PublishSubject.create()
    private val _isRefreshSubject: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    private val _onRefreshSubject: PublishSubject<Unit> = PublishSubject.create()
    private var disposable: Disposable? = null

    private val _emptyTicket: MutableLiveData<Boolean> = MutableLiveData()
    private val _reservationListButton: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _message = SingleLiveEvent<String>()
    private val _buttonState: MutableLiveData<Boolean> = MutableLiveData(true)
    private val _isRefresh: MutableLiveData<Boolean> = MutableLiveData()
    val isRefresh: LiveData<Boolean>
        get() = _isRefresh

    val emptyTicket: LiveData<Boolean>
        get() = _emptyTicket

    val reservationListButton: LiveData<Unit>
        get() = _reservationListButton

    val message: LiveData<String>
        get() = _message

    val buttonState: LiveData<Boolean>
        get() = _buttonState

    private val _ticketList: MutableLiveData<ArrayList<Ticket>> = MutableLiveData()
    val ticketList: LiveData<ArrayList<Ticket>>
        get() = _ticketList

    init {
        compositeDisposable.addAll(
            reservationList.throttleFirst(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _reservationListButton.call() },

            _onRefreshSubject.observeOn(AndroidSchedulers.mainThread())
                .doOnNext { _isRefreshSubject.onNext(true) }
                .map { _ticketList.value?.size }
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
                        _ticketList.value = allTicketList(body.data)
                    } else {
                        showEmptyTicket()
                    }
                } else {
                    _serverError.value = "서버로부터 이용권을 불러오는데 실패했습니다"
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.call()
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "이용권을 불러오는데 실패했습니다"
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

    private fun allTicketList(item: HomeResponseInfo): ArrayList<Ticket> {
        val newTicketList = arrayListOf<Ticket>()

        val useList = mapperUseTicket(item.tickets)
        val pauseList = mapperPauseTicket(item.pausedTickets)
        val expiredList = mapperExpiredTicket(item.expiredTickets)

        if (useList.isNotEmpty()) {
            for (i in useList.indices) {
                newTicketList.add(useList[i])
            }
        }

        if (pauseList.isNotEmpty()) {
            for (i in pauseList.indices) {
                newTicketList.add(pauseList[i])
            }
        }

        if (expiredList.isNotEmpty()) {
            for (i in expiredList.indices) {
                newTicketList.add(expiredList[i])
            }
        }

        return newTicketList
    }

    private fun mapperUseTicket(item: List<Ticket>): List<Ticket> {
        return item.map {
            Ticket(
                viewType = USE_TICKET,
                id = it.id,
                name = it.name,
                type = it.type,
                period = it.period,
                totalCount = it.totalCount,
                usedCount = it.usedCount,
                onlyOnceToday = it.onlyOnceToday,
                remainingCount = it.remainingCount,
                isUnlimited = it.isUnlimited,
                startDate = it.startDate,
                endDate = it.endDate,
                allowToBook = it.allowToBook,
                ignoreLimit = it.ignoreLimit, //DONETODO: flag to ignore max reservation limit to see opened reseration time schedule
                multipleReservation = it.multipleReservation,//DONETODO flag to select multiple time in a reservation for songdo branch
                maxMultipleCount = it.maxMultipleCount,//DONETODO count for max reservation selection in a reservation for songdo branch
                pasuePeriods = it.pasuePeriods,
                totalPrice = it.totalPrice,
                maxReservationCount = it.maxReservationCount
            )
        }
    }

    private fun mapperPauseTicket(item: List<Ticket>): List<Ticket> {
        return item.map {
            Ticket(
                viewType = PAUSE_TICKET,
                id = it.id,
                name = it.name,
                type = it.type,
                period = it.period,
                totalCount = it.totalCount,
                usedCount = it.usedCount,
                onlyOnceToday = it.onlyOnceToday,
                remainingCount = it.remainingCount,
                isUnlimited = it.isUnlimited,
                startDate = it.startDate,
                endDate = it.endDate,
                allowToBook = it.allowToBook,
                ignoreLimit = it.ignoreLimit, //DONETODO: flag to ignore max reservation limit to see opened reseration time schedule
                multipleReservation = it.multipleReservation,//DONETODO flag to select multiple time in a reservation for songdo branch
                maxMultipleCount = it.maxMultipleCount,//DONETODO count for max reservation selection in a reservation for songdo branch
                pasuePeriods = it.pasuePeriods,
                totalPrice = it.totalPrice
            )
        }
    }

    private fun mapperExpiredTicket(item: List<Ticket>): List<Ticket> {
        return item.map {
            Ticket(
                viewType = EXPIRED_TICKET,
                id = it.id,
                name = it.name,
                type = it.type,
                period = it.period,
                totalCount = it.totalCount,
                usedCount = it.usedCount,
                onlyOnceToday = it.onlyOnceToday,
                remainingCount = it.remainingCount,
                isUnlimited = it.isUnlimited,
                startDate = it.startDate,
                endDate = it.endDate,
                allowToBook = it.allowToBook,
                ignoreLimit = it.ignoreLimit, //DONETODO: flag to ignore max reservation limit to see opened reseration time schedule
                multipleReservation = it.multipleReservation,//DONETODO flag to select multiple time in a reservation for songdo branch
                maxMultipleCount = it.maxMultipleCount,//DONETODO count for max reservation selection in a reservation for songdo branch
                pasuePeriods = it.pasuePeriods,
                totalPrice = it.totalPrice
            )
        }
    }
}