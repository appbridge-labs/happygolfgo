package com.artilearn.happygolfgo.ui.tickettime

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

    private val _ticketTime: MutableLiveData<ArrayList<TicketTime>> = MutableLiveData()
    private val _emptyTicketTime: MutableLiveData<Boolean> = MutableLiveData()

    val ticketTime: LiveData<ArrayList<TicketTime>> get() = _ticketTime
    val emptyTicketTime: LiveData<Boolean> get() = _emptyTicketTime

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
                        if (body.data.availableTimes.isNotEmpty()) {
                            hideEmptyTicketTimeView()
                            _ticketTime.value = body.data.availableTimes as ArrayList<TicketTime>
                        } else {
                            showEmptyTicketTimeView()
                        }
                    } else {
                        _serverError.value = response.message()
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
                        if (body.data.availableTimes.isNotEmpty()) {
                            hideEmptyTicketTimeView()
                            _ticketTime.value = body.data.availableTimes as ArrayList<TicketTime>
                        } else {
                            showEmptyTicketTimeView()
                        }
                    } else {
                        _serverError.value = response.errorBody().toString()
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

    private fun showEmptyTicketTimeView() {
        _emptyTicketTime.value = true
    }

    private fun hideEmptyTicketTimeView() {
        _emptyTicketTime.value = false
    }
}