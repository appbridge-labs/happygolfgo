package com.artilearn.happygolfgo.ui.ticketmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.ticketmanager.source.TicketManagerRepository
import com.artilearn.happygolfgo.ui.ticketmanager.pause.model.PauseModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class TicketManagerViewModel(
    private val repository: TicketManagerRepository
) : BaseViewModel() {

    private val _tabPosition: MutableLiveData<Int> = MutableLiveData(0)
    val tabPosition: LiveData<Int>
        get() = _tabPosition

    private val _pauseTicketList: MutableLiveData<List<PauseModel>> = MutableLiveData()
    val pauseTicketList: LiveData<List<PauseModel>>
        get() = _pauseTicketList

    private val _emptyPauseTicket: MutableLiveData<Boolean> = MutableLiveData(false)
    val emptyPauseTicket: LiveData<Boolean>
        get() = _emptyPauseTicket

    fun tabItemClick(position: Int) {
        _tabPosition.value = position
    }

    fun getPauseTickets() {
        repository.getPauseTickets()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .subscribe({ response ->
                checkPauseTicketList(response)
            }, {
                println("getPauseTickets error: $it")
            }).addTo(compositeDisposable)
    }

    fun getExpiredickets() {
        repository.getExpiredTickets()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                println("getExpiredickets success")
            }, {
                println("getExpiredickets error: $it")
            }).addTo(compositeDisposable)
    }

    private fun checkPauseTicketList(list: List<PauseModel>) {
        isEmptyPauseTicket(list.isEmpty())
        _pauseTicketList.value = list
    }

    private fun isEmptyPauseTicket(result: Boolean) = _emptyPauseTicket.postValue(result)
}