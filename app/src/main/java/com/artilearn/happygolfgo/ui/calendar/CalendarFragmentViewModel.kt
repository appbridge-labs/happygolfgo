package com.artilearn.happygolfgo.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel

class CalendarFragmentViewModel : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val _calendarLoading: MutableLiveData<Boolean> = MutableLiveData()

    val calendarLoading: LiveData<Boolean> get() = _calendarLoading
}