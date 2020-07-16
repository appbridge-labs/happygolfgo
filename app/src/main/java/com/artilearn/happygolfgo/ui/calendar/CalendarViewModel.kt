package com.artilearn.happygolfgo.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel

class CalendarViewModel : BaseViewModel() {

    private val _calendarLoading: MutableLiveData<Boolean> = MutableLiveData()

    val calendarLoading: LiveData<Boolean> get() = _calendarLoading

    init {

    }


    private fun showCalendarView() {
        _calendarLoading.value = true
    }

    private fun hideCalendarView() {
        _calendarLoading.value = false
    }
}