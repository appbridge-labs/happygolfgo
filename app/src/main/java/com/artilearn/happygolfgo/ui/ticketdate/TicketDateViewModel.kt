package com.artilearn.happygolfgo.ui.ticketdate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.applandeo.materialcalendarview.EventDay
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.ticketdate.source.TicketDateRepository
import com.artilearn.happygolfgo.util.convertCalendar
import com.artilearn.happygolfgo.util.convertDate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class TicketDateViewModel(
    private val ticketDateRepository: TicketDateRepository
) : BaseViewModel() {

    private val _isCalendarLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val _eventDayList: MutableLiveData<List<EventDay>> = MutableLiveData()
    private val _availableMinDate: MutableLiveData<Calendar> = MutableLiveData()
    private val _availableMaxDate: MutableLiveData<Calendar> = MutableLiveData()

    val eventDayList: LiveData<List<EventDay>> get() = _eventDayList
    val availableMinDate: LiveData<Calendar> get() = _availableMinDate
    val availableMaxDate: LiveData<Calendar> get() = _availableMaxDate
    val isCalendarLoading: LiveData<Boolean> get() = _isCalendarLoading

    var ticketId: Int? = null
    private var calendarMin: Calendar? = null
    private var calendarMax: Calendar? = null
    private var eventDays = mutableListOf<EventDay>()
    var eventList = arrayListOf<Calendar>()

    fun selectDate(type: Int) {
        when (type) {
            0 -> { // lesson
                selectLessonDate()
            }
            1 -> { // group lesson

            }
            2 -> { // plate
                selectPlateDate()
            }
            3 -> { // address

            }
            4 -> { // locker

            }
            5 -> { // other

            }
        }
    }

    private fun selectPlateDate() {
        ticketId?.let {
            compositeDisposable.add(
                ticketDateRepository.selectPlateDate(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showLoading() }
                    .doAfterTerminate { hideLoading() }
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
                        when (t.message) {
                            "Network Error" -> {
                                _networkFail.value = Unit
                            }
                            else -> {
                                _serverError.value = t.message
                            }
                        }
                    })
            )
        }
    }

    private fun selectLessonDate() {
        ticketId?.let {
            compositeDisposable.add(
                ticketDateRepository.selectLessonDate(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showLoading() }
                    .doAfterTerminate { hideLoading() }
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
                        when (t.message) {
                            "Network Error" -> {
                                _networkFail.value = Unit
                            }
                            else -> {
                                _serverError.value = t.message
                            }
                        }
                    })
            )
        }
    }

    private fun convertToEventDay(available: List<String>?, event: List<String>?) {
        available?.let {
            with(it) {
                if (isNotEmpty()) {
                    val min = Collections.min(available)
                    val max = Collections.max(available)
                    val minDate = convertDate(min)
                    val maxDate = convertDate(max)

                    minDate?.let { calendarMin = convertCalendar(it) }
                    maxDate?.let { calendarMax = convertCalendar(it) }
                }
            }
        }

        event?.let {
            with(it) {
                if (isNotEmpty()) {
                    for (i in 0 until size) {
                        val date = convertDate(this[i])
                        date?.let { eventList.add(convertCalendar(it)) }
                    }
                }
            }
        }

        confirmDates()
    }

    private fun confirmDates() {
        if (eventList.isNotEmpty()) {
            for (i in 0 until eventList.size) {
                eventDays.add(EventDay(eventList[i], R.drawable.ic_calendar_event))
            }

            _eventDayList.value = eventDays
        }

        calendarMin?.let { _availableMinDate.value = it }
        calendarMax?.let { _availableMaxDate.value = it }

        _isCalendarLoading.value = true
    }

}