package com.artilearn.happygolfgo.ui.home.reservation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.home.source.HomeRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ReservationViewModel(
    private val repository: HomeRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _emptyTicket: MutableLiveData<Boolean> = MutableLiveData()
    private val _tickets: MutableLiveData<ArrayList<Ticket>> = MutableLiveData()

    val emptyTicket: LiveData<Boolean>
        get() = _emptyTicket

    val tickets: LiveData<ArrayList<Ticket>>
        get() = _tickets

    init {
        getTicket()
    }

    fun getTicket() {
        compositeDisposable.add(
            repository.requestHomeTicket()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({
                    val body = it.body()
                    if (it.isSuccessful) {
                        if (body != null && body.data.tickets.isNotEmpty()) {
                            hideEmptyTicket()
                            _tickets.value = body.data.tickets as ArrayList<Ticket>
                        } else {
                            showEmptyTicket()
                        }
                    } else {
                        _serverError.value = it.message()
                    }
                }, {
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

    private fun showEmptyTicket() {
        _emptyTicket.value = true
    }

    private fun hideEmptyTicket() {
        _emptyTicket.value = false
    }
}