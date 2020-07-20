package com.artilearn.happygolfgo.ui.tickettime

import android.util.Log
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.tickettime.source.TicketTimeRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TicketTimeViewModel(
    private val ticketTimeRepository: TicketTimeRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

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
                    Log.d(TAG, "selectTicketPlateTime: ${response.message()}")
                    Log.d(TAG, "selectTicketPlateTime: ${response.headers()}")
                    Log.d(TAG, "selectTicketPlateTime: ${response.errorBody()}")
                    Log.d(TAG, "selectTicketPlateTime: ${response.raw()}")
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        Log.d(TAG, "selectTicketPlateTime: ${body.data.availableTimes}")
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
                    Log.d(TAG, "ticketLesson: ${response.body()}")
                }, { t ->
                    Log.d(TAG, "ticketLesson: ${t.message}")
                })
        )
    }
}