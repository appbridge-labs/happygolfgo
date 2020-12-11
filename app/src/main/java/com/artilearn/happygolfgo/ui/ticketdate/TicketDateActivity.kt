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
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TicketDateActivity : BaseActivity<ActivityTicketDateBinding>(R.layout.activity_ticket_date) {
    private val TAG = javaClass.simpleName
    private val viewModel: TicketDateViewModel by viewModel()
    private val ticket by lazy { intent.getParcelableExtra<Ticket>(TICKET) }
    private lateinit var events: MutableList<EventDay>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initView()
        //initCalendarListener()

        events = mutableListOf()
        initRangeMonth()
        initDotList()
        initInvisibleDays()
        initCalendarClickListener()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
//            serverError.observe(this@TicketDateActivity, Observer {
//                showError(it)
//            })
//            networkFail.observe(this@TicketDateActivity, Observer {
//                showNetworkFail()
//            })
//            availablieList.observe(this@TicketDateActivity, Observer {
//                Log.d(TAG, "availablieList observe: ")
//                val min = it.firstOrNull()
//                val max = it.lastOrNull()
//                Log.d(TAG, "min: ${min?.get(Calendar.YEAR)}년 ${min?.get(Calendar.MONTH)?.plus(1)}월, ${min?.get(Calendar.DAY_OF_MONTH)}일")
//                Log.d(TAG, "max: ${max?.get(Calendar.YEAR)}년 ${max?.get(Calendar.MONTH)?.plus(1)}월, ${max?.get(Calendar.DAY_OF_MONTH)}일")
//
//                min?.add(Calendar.DAY_OF_MONTH, min.get(Calendar.DAY_OF_MONTH))
//                max?.add(Calendar.MONTH, max.get(Calendar.MONTH))
//
//                with(binding.calendarview) {
//                    setMinimumDate(min!!)
//                    setMaximumDate(max!!)
//                }
//            })
//            dotList.observe(this@TicketDateActivity, Observer {
//                Log.d(TAG, "dotList observe: ${it.size}")
//                binding.calendarview.setEvents(it)
//            })
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






    private fun initDotList() {
        val calendar1 = Calendar.getInstance()
        calendar1.add(Calendar.DAY_OF_MONTH, 3)

        val calendar2 = Calendar.getInstance()
        calendar2.add(Calendar.DAY_OF_MONTH, 5)

        val calendar3 = Calendar.getInstance()
        calendar3.add(Calendar.DAY_OF_MONTH, 7)

        events.add(EventDay(calendar1, R.drawable.bg_dot_blue))
        events.add(EventDay(calendar2, R.drawable.bg_dot_red))
        events.add(EventDay(calendar3, R.drawable.bg_dot_white))

        binding.calendarview.setEvents(events)
    }

    private fun initRangeMonth() {
        val min = Calendar.getInstance()
        min.add(Calendar.DAY_OF_MONTH, -1)

        val max = Calendar.getInstance()
        max.add(Calendar.MONTH, 1)

        binding.calendarview.setMinimumDate(min)
        binding.calendarview.setMaximumDate(max)
    }

    private fun initInvisibleDays() {
        val calendars = mutableListOf<Calendar>()

        val first = Calendar.getInstance()
        first.add(Calendar.DAY_OF_MONTH, 1)

        val second = Calendar.getInstance()
        second.add(Calendar.DAY_OF_MONTH, 4)

        val third = Calendar.getInstance()
        third.add(Calendar.DAY_OF_MONTH, 6)

        val fourth = Calendar.getInstance()
        fourth.add(Calendar.DAY_OF_MONTH, 12)

        val fifth = Calendar.getInstance()
        fifth.add(Calendar.DAY_OF_MONTH, 15)

        val final = Calendar.getInstance()
        final.add(Calendar.DAY_OF_MONTH, 27)

        calendars.add(first)
        calendars.add(second)
        calendars.add(third)
        calendars.add(fourth)
        calendars.add(fifth)
        calendars.add(final)

        binding.calendarview.setDisabledDays(calendars)
    }

    private fun initCalendarClickListener() {
        binding.calendarview.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                if (eventDay.isEnabled) {
                    val day = "${eventDay.calendar[Calendar.YEAR]}년 " +
                            "${eventDay.calendar[Calendar.MONTH].plus(1)}월 " +
                            "${eventDay.calendar[Calendar.DAY_OF_MONTH]}일"
                    Snackbar.make(binding.parentLayout, day + "예약할수없어요~", 700).also { snackbar ->
                        snackbar.setAction("확인") { snackbar.dismiss() }
                    }.show()
                } else {
                    Snackbar.make(binding.parentLayout, "click ok", 700).also { snackbar ->
                        snackbar.setAction("확인") { snackbar.dismiss() }
                    }.show()
                }
            }
        })
    }

}