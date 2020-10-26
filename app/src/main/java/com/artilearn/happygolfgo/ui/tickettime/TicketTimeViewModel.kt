package com.artilearn.happygolfgo.ui.tickettime

import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.data.tickettime.source.TicketTimeRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class TicketTimeViewModel(
    private val ticketTimeRepository: TicketTimeRepository
) : BaseViewModel() {

    private val _ticketTime: SingleLiveEvent<ArrayList<TicketTime>> = SingleLiveEvent()
    private val _emptyTicketTime: SingleLiveEvent<Boolean> = SingleLiveEvent()

    val ticketTime: LiveData<ArrayList<TicketTime>>
        get() = _ticketTime

    val emptyTicketTime: LiveData<Boolean>
        get() = _emptyTicketTime

    fun selectTime(type: Int, date: String, id: Int) {
        when (type) {
            0 -> selectTicketLessonTime(date, id)
            2 -> selectTicketPlateTime(date, id)
        }
    }

    private fun selectTicketPlateTime(date: String, id: Int) {
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
                    "Network Error" -> _networkFail.value = Unit
                    else -> _serverError.value = t.message
                }
            }).addTo(compositeDisposable)
    }

    private fun selectTicketLessonTime(date: String, id: Int) {
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
                    _serverError.value = response.message()
                }
            }, { t ->
                when (t.message) {
                    "Network Error" -> _networkFail.value = Unit
                    else -> _serverError.value = t.message
                }
            }).addTo(compositeDisposable)
    }

    private fun showEmptyTicketTimeView() {
        _emptyTicketTime.value = true
    }

    private fun hideEmptyTicketTimeView() {
        _emptyTicketTime.value = false
    }
}