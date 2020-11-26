package com.artilearn.happygolfgo.ui.tickettime

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.data.tickettime.source.TicketTimeRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class TicketTimeViewModel(
    private val ticketTimeRepository: TicketTimeRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _ticketTime: MutableLiveData<ArrayList<TicketTime>> = MutableLiveData()
    private val _emptyTicketTime: MutableLiveData<Boolean> = MutableLiveData()

    val ticketTime: LiveData<ArrayList<TicketTime>> get() = _ticketTime
    val emptyTicketTime: LiveData<Boolean> get() = _emptyTicketTime

    fun selectTime(type: Int, date: String, id: Int) {
        when (type) {
            0 -> {
                selectTicketLessonTime(date, id)
            }
            1 -> {}
            2 -> {
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
                .doOnError { t ->
                    try {
                        throw RuntimeException(TAG)
                    } catch (e: Exception) {
                        FirebaseCrashlytics.getInstance().log("$t\n${t.localizedMessage}")
                    }
                }
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
                .doOnError { t ->
                    try {
                        throw RuntimeException(TAG)
                    } catch (e: Exception) {
                        FirebaseCrashlytics.getInstance().log("$t\n${t.localizedMessage}")
                    }
                }
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