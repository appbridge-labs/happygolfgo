package com.artilearn.happygolfgo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.home.source.HomeRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel(
    private val homeRepository: HomeRepository
) : BaseViewModel() {

    private val _myPage: MutableLiveData<Unit> = MutableLiveData()
    private val _alram: MutableLiveData<Unit> = MutableLiveData()
    private val _emptyTicket: MutableLiveData<Boolean> = MutableLiveData()
    private val _tickets: MutableLiveData<ArrayList<Ticket>> = MutableLiveData()

    val myPage: LiveData<Unit> get() = _myPage
    val alram: LiveData<Unit> get() = _alram
    val emptyTicket: LiveData<Boolean> get() = _emptyTicket
    val tickets: LiveData<ArrayList<Ticket>> get() = _tickets

    init {
        requestTicket()
    }

    fun onMyPageClick() {
        _myPage.value = Unit
    }

    fun onAlramClick() {
        _alram.value = Unit
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