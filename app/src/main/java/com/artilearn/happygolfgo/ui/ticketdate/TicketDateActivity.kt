package com.artilearn.happygolfgo.ui.ticketdate

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
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
import org.koin.android.ext.android.bind
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
                showError(it)
            })
            networkFail.observe(this@TicketDateActivity, Observer {
                showNetworkFail()
            })
            availablieList.observe(this@TicketDateActivity, Observer {
                Log.d(TAG, "availablieList observe: ")
                val min = it.firstOrNull()
                val max = it.lastOrNull()
                Log.d(TAG, "min: ${min?.get(Calendar.YEAR)}년 ${min?.get(Calendar.MONTH)?.plus(1)}월, ${min?.get(Calendar.DAY_OF_MONTH)}일")
                Log.d(TAG, "max: ${max?.get(Calendar.YEAR)}년 ${max?.get(Calendar.MONTH)?.plus(1)}월, ${max?.get(Calendar.DAY_OF_MONTH)}일")

                with(binding.calendarview) {
                    setMinimumDate(min!!)
                    setMaximumDate(max!!)
                }
            })
            dotList.observe(this@TicketDateActivity, Observer {
                Log.d(TAG, "dotList observe: ${it.size}")
                binding.calendarview.setEvents(it)
            })
//            eventDayList.observe(this@TicketDateActivity, Observer {
//                binding.calendarview.setEvents(it)
//            })
//            availableMinDate.observe(this@TicketDateActivity, Observer {
//                Log.d(TAG, "min result: ${it[Calendar.YEAR]}년 ${it[Calendar.MONTH].plus(1)}월 ${it[Calendar.DAY_OF_MONTH]}일")
//
//                val invisibleCalendar: MutableList<Calendar> = mutableListOf()
//                val first = Calendar.getInstance()
//                val second = Calendar.getInstance()
//                first.add(Calendar.DAY_OF_MONTH, 1)
//                second.add(Calendar.DAY_OF_MONTH, 2)
//                invisibleCalendar.add(first)
//                invisibleCalendar.add(second)
//                Log.d(TAG, "예약불가1: ${first[Calendar.YEAR]}년 ${first[Calendar.MONTH].plus(1)}월, ${first[Calendar.DAY_OF_MONTH]}일")
//                Log.d(TAG, "예약불가2: ${second[Calendar.YEAR]}년 ${second[Calendar.MONTH].plus(1)}월, ${second[Calendar.DAY_OF_MONTH]}일")
//
//                binding.calendarview.setMinimumDate(it)
//                binding.calendarview.setDisabledDays(invisibleCalendar)
//            })
//            availableMaxDate.observe(this@TicketDateActivity, Observer {
//                Log.d(TAG, "max result: ${it[Calendar.YEAR]}년 ${it[Calendar.MONTH].plus(1)}월 ${it[Calendar.DAY_OF_MONTH]}일")
//                binding.calendarview.setMaximumDate(it)
//            })
        }
    }

    private fun initView() {
//        val invisibleCalendar: MutableList<Calendar> = mutableListOf()
//        val first = Calendar.getInstance()
//        val second = Calendar.getInstance()
//        first.add(Calendar.DAY_OF_MONTH, 1)
//        second.add(Calendar.DAY_OF_MONTH, 3)
//        invisibleCalendar.add(first)
//        invisibleCalendar.add(second)
//        Log.d(TAG, "예약불가1: ${first[Calendar.YEAR]}년 ${first[Calendar.MONTH].plus(1)}월, ${first[Calendar.DAY_OF_MONTH]}일")
//        Log.d(TAG, "예약불가2: ${second[Calendar.YEAR]}년 ${second[Calendar.MONTH].plus(1)}월, ${second[Calendar.DAY_OF_MONTH]}일")
//
//        val dotList: MutableList<EventDay> = mutableListOf()
//        val dot1 = Calendar.getInstance()
//        val dot2 = Calendar.getInstance()
//        dot1.add(Calendar.DAY_OF_MONTH, 5)
//        dot2.add(Calendar.DAY_OF_MONTH, 6)
//        dotList.add(EventDay(dot1, R.drawable.ic_calendar_event))
//        dotList.add(EventDay(dot2, R.drawable.ic_calendar_event))
//        Log.d(TAG, "예약1: ${dot1[Calendar.YEAR]}년 ${dot1[Calendar.MONTH].plus(1)}월, ${dot1[Calendar.DAY_OF_MONTH]}일")
//        Log.d(TAG, "예약2: ${dot2[Calendar.YEAR]}년 ${dot2[Calendar.MONTH].plus(1)}월, ${dot2[Calendar.DAY_OF_MONTH]}일")
//
//        val min = Calendar.getInstance()
//        val max = Calendar.getInstance()
//        min.add(Calendar.DAY_OF_MONTH, -10)
//        max.add(Calendar.DAY_OF_MONTH, 10)
//        Log.d(TAG, "최소: ${min[Calendar.YEAR]}년 ${min[Calendar.MONTH].plus(1)}월, ${min[Calendar.DAY_OF_MONTH]}일")
//        Log.d(TAG, "최대: ${max[Calendar.YEAR]}년 ${max[Calendar.MONTH].plus(1)}월, ${max[Calendar.DAY_OF_MONTH]}일")

        initToolbar(binding.calendarToolbar.toolbar, getString(R.string.ticketdate_toolbar_title), true)
        ticket?.let {
            viewModel.ticketId = it.id
            viewModel.selectDate(it.type)
        }

//        with(binding.calendarview) {
//            setMinimumDate(min)
//            setMaximumDate(max)
//            setDisabledDays(invisibleCalendar)
//            setEvents(dotList)
//        }
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

}