package com.artilearn.happygolfgo.ui.tickettime

import android.content.Intent
import android.os.Bundle
<<<<<<< HEAD
=======
import android.util.Log
import androidx.activity.viewModels
>>>>>>> hot-test-3
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.*
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.databinding.ActivityTicketTimeBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmActivity
import com.artilearn.happygolfgo.ui.splash.SplashActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class TicketTimeActivity : BaseActivity<ActivityTicketTimeBinding>(R.layout.activity_ticket_time) {

    private val viewModel: TicketTimeViewModel by viewModel()
    private lateinit var adapter: TicketTimeAdapter
    private val reservation = "reservation"

    private val ticket by lazy { intent.getParcelableExtra<Ticket>(TICKET) }
    private val date by lazy { intent.getStringExtra(SELECTION_DATE) }
    private val timeChange by lazy { intent.getIntExtra(PLATE_TIME_CHANGE, 0) }

<<<<<<< HEAD
=======
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
>>>>>>> hot-test-3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initView()
        initAdapter()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            serverError.observe(this@TicketTimeActivity, Observer {
<<<<<<< HEAD
=======
                //showError(it)
>>>>>>> hot-test-3
                showToast(ToastType.ERROR, it)
            })
            networkFail.observe(this@TicketTimeActivity, Observer {
                showNetworkFail()
            })
            appRefresh.observe(this@TicketTimeActivity, Observer {
                goRefresh()
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