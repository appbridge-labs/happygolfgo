package com.artilearn.happygolfgo.ui.ticketdate

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.TICKET
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.ActivityTicketDateBinding
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.snackbar
import kotlinx.android.synthetic.main.activity_ticket_date.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import java.util.*

class TicketDateActivity : BaseActivity<ActivityTicketDateBinding>(R.layout.activity_ticket_date) {
    private val TAG = javaClass.simpleName

    private var eventList: ArrayList<Calendar> = arrayListOf()
    private var ticket: Ticket? = null
    private lateinit var myApplication: MyApplication
    private val viewModel: TicketDateViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TicketDateViewModel(
                    myApplication.ticketDateRepository
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
        initViewModelObserve()
        initView()
        initCalendarListener()
    }

    private fun inject() {
        myApplication = application as MyApplication
        binding.vm = viewModel

        ticket = intent?.getParcelableExtra(TICKET)
        ticket?.let {
            viewModel.ticketId = it.id
            viewModel.selectDate(it.type)
        }

        initViewModelObserve()
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            serverError.observe(this@TicketDateActivity, Observer {
                showError(it)
            })
            networkFail.observe(this@TicketDateActivity, Observer {
                showNetworkFail()
            })
            eventDayList.observe(this@TicketDateActivity, Observer {
                binding.calendarview.setEvents(it)
            })
            availableMinDate.observe(this@TicketDateActivity, Observer {
                binding.calendarview.setMinimumDate(it)
            })
            availableMaxDate.observe(this@TicketDateActivity, Observer {
                binding.calendarview.setMaximumDate(it)
            })
        }
    }

    private fun initView() {
        ticket = intent.getParcelableExtra(TICKET)
        initToolbar(calendar_toolbar.toolBar, getString(R.string.ticketdate_toolbar_title), true)
    }

    private fun initCalendarListener() {
        binding.calendarview.setOnDayClickListener(
            object : OnDayClickListener {
                override fun onDayClick(eventDay: EventDay) {
                    val month = eventDay.calendar.get(Calendar.MONTH)
                    val date =
                        "${eventDay.calendar.get(Calendar.YEAR)}-${month + 1}-${eventDay.calendar.get(
                            Calendar.DAY_OF_MONTH
                        )}"

                    if (viewModel.eventList.contains(eventDay.calendar)) {
                        binding.parentLayout.snackbar(getString(R.string.ticketdate_impossible_ticket))
                    } else {
                        if (eventDay.isEnabled) {
                            binding.parentLayout.snackbar(getString(R.string.ticketdate_impossible_calendar))
                        } else {
                            binding.parentLayout.snackbar("else 예약가능\n$date")
                        }
                    }
                }
            }
        )
    }





}