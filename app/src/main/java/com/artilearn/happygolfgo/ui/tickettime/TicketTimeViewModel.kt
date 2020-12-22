package com.artilearn.happygolfgo.ui.tickettime

import android.accounts.NetworkErrorException
<<<<<<< HEAD
=======
import android.util.Log
>>>>>>> hot-test-3
import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.data.tickettime.source.TicketTimeRepository
<<<<<<< HEAD
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
=======
>>>>>>> hot-test-3
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class TicketTimeViewModel(
    private val ticketTimeRepository: TicketTimeRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
<<<<<<< HEAD
    private val _ticketTime: SingleLiveEvent<ArrayList<TicketTime>> = SingleLiveEvent()
    private val _emptyTicketTime: SingleLiveEvent<Boolean> = SingleLiveEvent()

    val ticketTime: LiveData<ArrayList<TicketTime>>
        get() = _ticketTime
=======
    private val _ticketTime: MutableLiveData<ArrayList<TicketTime>> = MutableLiveData()
    private val _emptyTicketTime: MutableLiveData<Boolean> = MutableLiveData()
>>>>>>> hot-test-3

    val emptyTicketTime: LiveData<Boolean>
        get() = _emptyTicketTime

    fun selectTime(type: Int, date: String, id: Int) {
        when (type) {
            0 -> selectTicketLessonTime(date, id)
            2 -> selectTicketPlateTime(date, id)
        }
    }

    private fun selectTicketPlateTime(date: String, id: Int) {
<<<<<<< HEAD
        ticketTimeRepository.selectPlateTime(date, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.availableTimes.isNotEmpty()) {
                        hideEmptyTicketTimeView()
                        _ticketTime.value = body.data.availableTimes as ArrayList<TicketTime>
=======
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
>>>>>>> hot-test-3
                    } else {
                        showEmptyTicketTimeView()
                    }
<<<<<<< HEAD
                } else {
                    _serverError.value = "서버로부터 데이터를 불러오는데 실패했습니다"
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
=======
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
>>>>>>> hot-test-3
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    private fun selectTicketLessonTime(date: String, id: Int) {
<<<<<<< HEAD
        ticketTimeRepository.selectLessonTime(date, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.availableTimes.isNotEmpty()) {
                        hideEmptyTicketTimeView()
                        _ticketTime.value = body.data.availableTimes as ArrayList<TicketTime>
=======
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
>>>>>>> hot-test-3
                    } else {
                        showEmptyTicketTimeView()
                    }
<<<<<<< HEAD
                } else {
                    _serverError.value = "서버로부터 데이터를 불러오는데 실패했습니다"
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
=======
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
>>>>>>> hot-test-3
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
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