package com.artilearn.happygolfgo.ui.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.calendar.source.CalendarRepository
import com.artilearn.happygolfgo.enummodel.SelectDateType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CalendarViewModel(
    private val calendarRepository: CalendarRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val _calendarLoading: MutableLiveData<Boolean> = MutableLiveData()

    val calendarLoading: LiveData<Boolean> get() = _calendarLoading

    var ticketId: Int? = null

    fun selectDate(type: SelectDateType) {
        when (type) {
            SelectDateType.PLATE -> selectPlateDate()
            SelectDateType.LESSON -> ""
        }
    }
//todo 타임아웃뜸....타임아웃 떴을때 달력 일단 보여주고 예외처리....액티비티 서버에러 시 \n 하고 서버상태가 안좋습니다 할 것
    private fun selectPlateDate() {
        ticketId?.let {
            compositeDisposable.add(
                calendarRepository.selectPlateDate(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showLoading() }
                    .doAfterTerminate { hideLoading() }
                    .subscribe({
                        Log.d(TAG, "selectPlateDate: ${it.body()?.data}")
                    }, {
                        Log.d(TAG, "selectPlateDate: ${it.message}")
                        when (it.message) {
                            "Network Error" -> {
                                _networkFail.value = Unit
                            }
                            else -> {
                                _serverError.value = it.message
                            }
                        }
                    })
            )
        }
    }

    private fun showCalendarView() {
        _calendarLoading.value = true
    }

    private fun hideCalendarView() {
        _calendarLoading.value = false
    }
}