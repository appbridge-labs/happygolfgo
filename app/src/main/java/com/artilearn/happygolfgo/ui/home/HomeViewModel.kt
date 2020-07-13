package com.artilearn.happygolfgo.ui.home

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.home.source.HomeRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class HomeViewModel(
    private val homeRepository: HomeRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val _ticketEmpty: MutableLiveData<Boolean> = MutableLiveData()
    private val _tickets: MutableLiveData<ArrayList<Ticket>> = MutableLiveData()

    val ticketEmpty: LiveData<Boolean> get() = _ticketEmpty
    val tickets: LiveData<ArrayList<Ticket>> get() = _tickets

    init {
        requestTicket()
    }

    fun requestTicket() {
        compositeDisposable.add(
            homeRepository.requestHomeTicket()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({
                    val body = it.body()
                    if (it.isSuccessful) {
                        if (body != null && body.data.tickets.isNotEmpty()) {
                            _ticketEmpty.value = false
                            _tickets.value = body.data.tickets as ArrayList<Ticket>
                            Log.d(TAG, "test: 아이템 있음, ${body.data}\n ${body.statusCode}")
                        } else {
                            _ticketEmpty.value = true
                        }
                    } else {
                        _serverError.value = Unit
                    }
                }, {
                    Log.d(TAG, "fail: $it\n${it.message}")
                    when (it) {
                        is HttpException -> {
                            _networkError.value = Unit
                        }
                        is NetworkErrorException -> {

                        }
                        else -> {
                            _serverError.value = Unit
                        }
                    }
                })
        )
    }
}