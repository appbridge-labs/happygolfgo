package com.artilearn.happygolfgo.ui.ticketmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel

class TicketManagerViewModel : BaseViewModel() {

    private val _tabPosition: MutableLiveData<Int> = MutableLiveData(0)
    val tabPosition: LiveData<Int>
        get() = _tabPosition

    fun tabItemClick(position: Int) {
        _tabPosition.value = position
    }
}