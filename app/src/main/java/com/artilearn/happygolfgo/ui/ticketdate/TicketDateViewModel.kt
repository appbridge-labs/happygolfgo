package com.artilearn.happygolfgo.ui.ticketdate

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.applandeo.materialcalendarview.EventDay
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.ticketdate.source.TicketDateRepository
import com.artilearn.happygolfgo.util.convertCalendar
import com.artilearn.happygolfgo.util.convertDate
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.util.*

class TicketDateViewModel(
    private val ticketDateRepository: TicketDateRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _isCalendarLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _eventDayList: MutableLiveData<List<EventDay>> = MutableLiveData()
    private val _availableMinDate: MutableLiveData<Calendar> = MutableLiveData()
    private val _availableMaxDate: MutableLiveData<Calendar> = MutableLiveData()

    val eventDayList: LiveData<List<EventDay>>
        get() = _eventDayList

    val availableMinDate: LiveData<Calendar>
        get() = _availableMinDate

    val availableMaxDate: LiveData<Calendar>
        get() = _availableMaxDate

    val isCalendarLoading: LiveData<Boolean>
        get() = _isCalendarLoading

    var ticketId: Int? = null
    private var calendarMin: Calendar? = null
    private var calendarMax: Calendar? = null
    private var eventDays = mutableListOf<EventDay>()
    var eventList = arrayListOf<Calendar>()
    var availableList = arrayListOf<Calendar>()

    private val _availablieList: MutableLiveData<List<Calendar?>> = MutableLiveData()
    val availablieList: LiveData<List<Calendar?>>
        get() = _availablieList
    private val _dotList: MutableLiveData<List<EventDay>> = MutableLiveData()
    val dotList: LiveData<List<EventDay>>
        get() = _dotList

    fun selectDate(type: Int) {
        when (type) {
            0 -> selectLessonDate()
            2 -> selectPlateDate()
        }
    }

    private fun selectPlateDate() {
        ticketId?.let {
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
                                FirebaseCrashlytics.getInstance()
                                    .log("$t\n${t.localizedMessage}\n${t.code()}\n${t.message()}")
                            }
                            _serverError.value = t.localizedMessage
                        }
                        else -> _serverError.value = t.localizedMessage
                    }
                }).addTo(compositeDisposable)
        }
    }

    private fun selectLessonDate() {
        ticketId?.let {
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
                                FirebaseCrashlytics.getInstance()
                                    .log("$t\n${t.localizedMessage}\n${t.code()}\n${t.message()}")
                            }
                            _serverError.value = t.localizedMessage
                        }
                        else -> _serverError.value = t.localizedMessage
                    }
                }).addTo(compositeDisposable)
        }
    }

    private fun convertToEventDay(available: List<String>, event: List<String>) {
        Log.d(TAG, "available: ${available.size}, dot: ${event.size}")
        for ((i, v) in available.withIndex()) {
            val date = convertDate(v)
            val calendar = if (date != null) {
                convertCalendar(date)
            } else {
                null
            }
            Log.d(TAG, "번호: $i, 예약가능 날짜: ${calendar?.get(Calendar.YEAR)}년 ${calendar?.get(Calendar.MONTH)?.plus(1)}월, ${calendar?.get(Calendar.DAY_OF_MONTH)}일")
        }

        for ((i, v) in event.withIndex()) {
            val date = convertDate(v)
            val calendar = if (date != null) {
                convertCalendar(date)
            } else {
                null
            }
            Log.d(TAG, "예약 번호: $i, 예약한 날짜: ${calendar?.get(Calendar.YEAR)}년 ${calendar?.get(Calendar.MONTH)?.plus(1)}월, ${calendar?.get(Calendar.DAY_OF_MONTH)}일")
        }

        if (available.isNotEmpty()) {
            availableList = arrayListOf()
            val calendarList = mutableListOf<Calendar?>()
            Log.d(TAG, "available.isNotEmpty()")
            for (i in available.indices) {
                val date = convertDate(available[i])
                val calendar = if (date != null) {
                    convertCalendar(date)
                } else {
                    null
                }
                availableList.add(calendar!!)
            }

            _availablieList.value = availableList
        } else {
            Log.d(TAG, "available.empty()")
        }

        if (event.isNotEmpty()) {
            eventList = arrayListOf()
            Log.d(TAG, "event.isNotEmpty()")
            for (i in event.indices) {
                val date = convertDate(event[i])
                val calendar = if (date != null) {
                    convertCalendar(date)
                } else {
                    null
                }
                eventList.add(calendar!!)
            }

            if (eventList.isNotEmpty()) {
                for (i in 0 until eventList.size) {
                    eventDays.add(EventDay(eventList[i], R.drawable.ic_calendar_event))
                }

                _dotList.value = eventDays
            }
        } else {
            eventList = arrayListOf()
            Log.d(TAG, "event.isNotEmpty()")
            for (i in event.indices) {
                val date = convertDate(event[i])
                val calendar = if (date != null) {
                    convertCalendar(date)
                } else {
                    null
                }
                eventList.add(Calendar.getInstance())
            }

            if (eventList.isNotEmpty()) {
                for (i in 0 until eventList.size) {
                    eventDays.add(EventDay(eventList[i], R.drawable.ic_calendar_event))
                }

                _dotList.value = eventDays
            }
        }

    }
}