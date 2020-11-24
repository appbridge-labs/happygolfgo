package com.artilearn.happygolfgo.ui.tickettime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.*
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.databinding.ActivityTicketTimeBinding
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmActivity
import com.artilearn.happygolfgo.util.MyApplication

class TicketTimeActivity : BaseActivity<ActivityTicketTimeBinding>(R.layout.activity_ticket_time) {

    private lateinit var adapter: TicketTimeAdapter
    private val reservation = "reservation"
    private lateinit var myApplication: MyApplication

    private val ticket by lazy {
        intent.getParcelableExtra<Ticket>(TICKET)
    }

    private val date by lazy {
        intent.getStringExtra(SELECTION_DATE)
    }

    private val timeChange by lazy {
        intent.getIntExtra(PLATE_TIME_CHANGE, 0)
    }

    private val viewModel: TicketTimeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TicketTimeViewModel(
                    myApplication.ticketTimeRepository
                ) as T
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
        initViewModelObserve()
        initView()
        initAdapter()
    }

    private fun inject() {
        myApplication = application as MyApplication
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            serverError.observe(this@TicketTimeActivity, Observer {
                showError(it)
            })
            networkFail.observe(this@TicketTimeActivity, Observer {
                showNetworkFail()
            })
        }
    }

    private fun initView() {
        initToolbar(binding.tickettimeToolbar.toolbar, getString(R.string.tickettime_toolbar_title), true)

        viewModel.selectTime(ticket.type, date, ticket.id)
    }

    private fun initAdapter() {
        adapter = TicketTimeAdapter { ticketTime ->
            goReserveConfirm(ticketTime)
        }
        binding.recyclerview.adapter = adapter
    }

    private fun goReserveConfirm(ticketTime: TicketTime) {
        val confirmIntent = Intent(this, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(TICKET, ticket)
                putExtra(TICKET_TIME, ticketTime)
                putExtra(RESERVATION_TYPE, reservation)
                putExtra(PLATE_TIME_CHANGE, timeChange)
            }
        startActivity(confirmIntent)
    }

}