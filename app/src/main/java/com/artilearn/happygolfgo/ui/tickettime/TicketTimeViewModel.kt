package com.artilearn.happygolfgo.ui.tickettime

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.data.tickettime.source.TicketTimeRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TicketTimeViewModel(
    private val ticketTimeRepository: TicketTimeRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val _ticketTime: MutableLiveData<ArrayList<TicketTime>> = MutableLiveData()

    val ticketTime: LiveData<ArrayList<TicketTime>> get() = _ticketTime

    fun selectTime(type: Int, date: String, id: Int) {
        when (type) {
            0 -> { // lesson
                selectTicketLessonTime(date, id)
            }
            1 -> {  } // group lesson
            2 -> { // plate
                selectTicketPlateTime(date, id)
            }
        }
    }

    private fun selectTicketPlateTime(date: String, id: Int) {
        compositeDisposable.add(
            ticketTimeRepository.selectPlateTime(date, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({ response ->
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        Log.d(TAG, "selectTicketPlateTime: ${body.data.availableTimes}")
                        _ticketTime.value = body.data.availableTimes as ArrayList<TicketTime>
                    } else {
                        _serverError.value = response.errorBody().toString()
                    }
                }, { t ->
                    Log.d(TAG, "ticketPlate: ${t.message}")
                })
        )
    }

    private fun selectTicketLessonTime(date: String, id: Int) {
        compositeDisposable.add(
            ticketTimeRepository.selectLessonTime(date, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({ response ->
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        Log.d(TAG, "ticketLesson: ${body.data.availableTimes}")
                        _ticketTime.value = body.data.availableTimes as ArrayList<TicketTime>
                    } else {
                        _serverError.value = response.errorBody().toString()
                    }
                }, { t ->
                    Log.d(TAG, "ticketLesson: ${t.message}")
                })
        )
    }
}