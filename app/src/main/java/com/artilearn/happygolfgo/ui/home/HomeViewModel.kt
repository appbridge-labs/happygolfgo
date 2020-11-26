package com.artilearn.happygolfgo.ui.home

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.home.source.HomeRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class HomeViewModel(
    private val homeRepository: HomeRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
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
                .doOnError { t ->
                    try {
                        throw RuntimeException(TAG)
                    } catch (e: Exception) {
                        FirebaseCrashlytics.getInstance().log("$t\n${t.localizedMessage}")
                    }
                }
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

    private fun showEmptyTicket() {
        _emptyTicket.value = true
    }

    private fun hideEmptyTicket() {
        _emptyTicket.value = false
    }

}