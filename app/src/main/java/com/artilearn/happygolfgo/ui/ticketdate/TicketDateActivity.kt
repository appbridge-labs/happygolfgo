package com.artilearn.happygolfgo.ui.ticketdate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.SELECTION_DATE
import com.artilearn.happygolfgo.constants.TICKET
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.ActivityTicketDateBinding
import com.artilearn.happygolfgo.ui.tickettime.TicketTimeActivity
import com.artilearn.happygolfgo.util.snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TicketDateActivity : BaseActivity<ActivityTicketDateBinding>(R.layout.activity_ticket_date) {
    private val TAG = javaClass.simpleName
    private val viewModel: TicketDateViewModel by viewModel()
    private val ticket by lazy { intent.getParcelableExtra<Ticket>(TICKET) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initView()
        initCalendarListener()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            serverError.observe(this@TicketDateActivity, Observer {
                //showError(it)
            })
            networkFail.observe(this@TicketDateActivity, Observer {
                showNetworkFail()
            })
        }
    }

    private fun initView() {
        initToolbar(binding.calendarToolbar.toolbar, getString(R.string.ticketdate_toolbar_title), true)
        ticket?.let {
            viewModel.ticketId = it.id
            viewModel.selectDate(it.type)
        }
    }

    private fun initCalendarListener() {
        binding.calendarview.setOnDayClickListener(
            object : OnDayClickListener {
                override fun onDayClick(eventDay: EventDay) {
                    Log.d(TAG, "click: ${eventDay.calendar.get(Calendar.MONTH).plus(1)}월 ${eventDay.calendar.get(Calendar.DAY_OF_MONTH)}일")
                    val date =
                        "${eventDay.calendar.get(Calendar.YEAR)}-${eventDay.calendar.get(Calendar.MONTH) + 1}" +
                                "-${eventDay.calendar.get(Calendar.DAY_OF_MONTH)}"
                    Log.d(TAG, "click date: ${eventDay.calendar.get(Calendar.MONTH).plus(1)}월 ${eventDay.calendar.get(Calendar.DAY_OF_MONTH)}일")
                    if (viewModel.eventList.contains(eventDay.calendar)) {
                        when {
                            ticket.onlyOnceToday -> binding.parentLayout.snackbar(getString(R.string.ticketdate_onlyoncetoday_ticket))
                            viewModel.availableList.contains(eventDay.calendar) -> goSelectionTime(date, ticket)
                            else -> binding.parentLayout.snackbar(getString(R.string.ticketdate_impossible_calendar))
                        }
                    } else {
                        when {
                            viewModel.availableList.contains(eventDay.calendar) -> goSelectionTime(date, ticket)
                            else -> binding.parentLayout.snackbar(getString(R.string.ticketdate_impossible_calendar))
                        }
                    }

                }
            }
        )
    }

    private fun goSelectionTime(date: String, ticket: Ticket?) {
        val goActivity = Intent(this, TicketTimeActivity::class.java)
            .apply {
                putExtra(TICKET, ticket)
                putExtra(SELECTION_DATE, date)
            }

        startActivity(goActivity)
    }

//    private fun initCalendarClickListener() {
//        binding.calendarview.setOnDayClickListener(object : OnDayClickListener {
//            override fun onDayClick(eventDay: EventDay) {
//                if (eventDay.isEnabled) {
//                    val day = "${eventDay.calendar[Calendar.YEAR]}년 " +
//                            "${eventDay.calendar[Calendar.MONTH].plus(1)}월 " +
//                            "${eventDay.calendar[Calendar.DAY_OF_MONTH]}일"
//                    Snackbar.make(binding.parentLayout, day + "예약할수없어요~", 700).also { snackbar ->
//                        snackbar.setAction("확인") { snackbar.dismiss() }
//                    }.show()
//                } else {
//                    Snackbar.make(binding.parentLayout, "click ok", 700).also { snackbar ->
//                        snackbar.setAction("확인") { snackbar.dismiss() }
//                    }.show()
//                }
//            }
//        })
//    }

}