package com.artilearn.happygolfgo.ui.ticketdate

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.ticketdate.source.TicketDateRepository
<<<<<<< HEAD
import com.artilearn.happygolfgo.util.ext.errorLog
=======
import com.artilearn.happygolfgo.util.convertCalendar
import com.artilearn.happygolfgo.util.convertDate
>>>>>>> hot-test-3
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
<<<<<<< HEAD
=======
import java.util.*
>>>>>>> hot-test-3

class TicketDateViewModel(
    private val ticketDateRepository: TicketDateRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
<<<<<<< HEAD

    private val _eventDayList: MutableLiveData<List<String>> = MutableLiveData()
    private val _availablieList: MutableLiveData<List<String>> = MutableLiveData()
    private val _goNext: MutableLiveData<Pair<Ticket, String>> = MutableLiveData()
=======
    private val _isCalendarLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val _eventDayList: MutableLiveData<List<EventDay>> = MutableLiveData()
    private val _availableMinDate: MutableLiveData<Calendar> = MutableLiveData()
    private val _availableMaxDate: MutableLiveData<Calendar> = MutableLiveData()
>>>>>>> hot-test-3

    val eventDayList: LiveData<List<String>>
        get() = _eventDayList

    val availablieList: LiveData<List<String>>
        get() = _availablieList

    val goNext: LiveData<Pair<Ticket, String>>
        get() = _goNext

    var ticketId: Int? = null

    fun selectDate(type: Int) {
        when (type) {
            0 -> selectLessonDate()
            2 -> selectPlateDate()
        }
    }

    private fun selectPlateDate() {
        ticketId?.let {
<<<<<<< HEAD
            ticketDateRepository.selectPlateDate(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .doOnError { t -> errorLog(TAG, t) }
                .subscribe({ response ->
                    val body = response.body()
                    with(response) {
                        if (isSuccessful && body != null) {
                            setMyDates(body.data.availableDates, body.data.eventDates)
                        } else {
                            _serverError.value = "서버로부터 데이터를 불러오는데 실패했습니다"
                        }
                    }
                }, { t ->
                    when (t) {
                        is NetworkErrorException -> _networkFail.value = Unit
                        is HttpException -> {
                            if (t.code() == 419) _appRefresh.call()
                            else _serverError.value = "데이터를 불러오는데 실패했습니다"
=======
            compositeDisposable.add(
                ticketDateRepository.selectPlateDate(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showLoading() }
                    .doAfterTerminate { hideLoading() }
                    .doOnError { t ->
                        try {
                            throw RuntimeException(TAG)
                        } catch (e: Exception) {
                            FirebaseCrashlytics.getInstance().log("$t\n${t.localizedMessage}")
                        }
                    }
                    .subscribe({ response ->
                        val body = response.body()
                        with(response) {
                            if (isSuccessful && body != null) {
                                convertToEventDay(body.data.availableDates, body.data.eventDates)
                            } else {
                                _serverError.value = response.message()
                            }
                        }
                    }, { t ->
                        when (t) {
                            is NetworkErrorException -> _networkFail.value = Unit
                            is HttpException -> {
                                try {
                                    throw RuntimeException(TAG)
                                } catch (e: Exception) {
                                    FirebaseCrashlytics.getInstance().log("$t\n${t.localizedMessage}\n${t.code()}\n${t.message()}")
                                }
                                _serverError.value = t.localizedMessage
                            }
                            else -> _serverError.value = t.localizedMessage
>>>>>>> hot-test-3
                        }
                        else -> _serverError.value = "알 수 없는 오류 발생"
                    }
                }).addTo(compositeDisposable)
        }
    }

    private fun selectLessonDate() {
        ticketId?.let {
<<<<<<< HEAD
            ticketDateRepository.selectLessonDate(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .doOnError { t -> errorLog(TAG, t) }
                .subscribe({ response ->
                    val body = response.body()
                    with(response) {
                        if (isSuccessful && body != null) {
                            setMyDates(body.data.availableDates, body.data.eventDates)
                        } else {
                            _serverError.value = "서버로부터 데이터를 불러오는데 실패했습니다"
=======
            compositeDisposable.add(
                ticketDateRepository.selectLessonDate(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showLoading() }
                    .doAfterTerminate { hideLoading() }
                    .doOnError { t ->
                        try {
                            throw RuntimeException(TAG)
                        } catch (e: Exception) {
                            FirebaseCrashlytics.getInstance().log("$t\n${t.localizedMessage}")
                        }
                    }
                    .subscribe({ response ->
                        val body = response.body()
                        with(response) {
                            if (isSuccessful && body != null) {
                                convertToEventDay(body.data.availableDates, body.data.eventDates)
                            } else {
                                _serverError.value = response.message()
                            }
                        }
                    }, { t ->
                        when (t) {
                            is NetworkErrorException -> _networkFail.value = Unit
                            is HttpException -> {
                                try {
                                    throw RuntimeException(TAG)
                                } catch (e: Exception) {
                                    FirebaseCrashlytics.getInstance().log("$t\n${t.localizedMessage}\n${t.code()}\n${t.message()}")
                                }
                                _serverError.value = t.localizedMessage
                            }
                            else -> _serverError.value = t.localizedMessage
>>>>>>> hot-test-3
                        }
                    }
                }, { t ->
                    when (t) {
                        is NetworkErrorException -> _networkFail.value = Unit
                        is HttpException -> {
                            if (t.code() == 419) _appRefresh.call()
                            else _serverError.value = "데이터를 불러오는데 실패했습니다"
                        }
                        else -> _serverError.value = "알 수 없는 오류 발생"
                    }
                }).addTo(compositeDisposable)
        }
    }

    private fun setMyDates(availablie: List<String>, events: List<String>) {
        _availablieList.value = availablie
        _eventDayList.value = events
    }

    fun calendarListener(ticket: Ticket, day: String) {
        _goNext.value = Pair(ticket, day)
    }

}