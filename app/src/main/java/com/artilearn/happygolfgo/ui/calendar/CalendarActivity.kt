package com.artilearn.happygolfgo.ui.calendar

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.EXTRA_ERROR_TEXT
import com.artilearn.happygolfgo.constants.EXTRA_INTENT
import com.artilearn.happygolfgo.constants.TICKET
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.ActivityCalendarBinding
import com.artilearn.happygolfgo.ui.common.ErrorActivity
import com.artilearn.happygolfgo.util.MyApplication
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class CalendarActivity : BaseActivity<ActivityCalendarBinding>(R.layout.activity_calendar) {

    private var ticket: Ticket? = null
    private lateinit var myApplication: MyApplication
    private val viewModel: CalendarViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CalendarViewModel(
                    myApplication.calendarRepository
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
        initViewModelObserve()
        initView()
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
            serverError.observe(this@CalendarActivity, Observer {

            })
            networkFail.observe(this@CalendarActivity, Observer {

            })
            eventDayList.observe(this@CalendarActivity, Observer {
                binding.calendarview.setEvents(it)
            })
            availableMinDate.observe(this@CalendarActivity, Observer {
                binding.calendarview.setMinimumDate(it)
            })
            availableMaxDate.observe(this@CalendarActivity, Observer {
                binding.calendarview.setMaximumDate(it)
            })
        }

    }

    private fun initView() {
        ticket = intent.getParcelableExtra(TICKET)
        initToolbar(calendar_toolbar.toolBar, getString(R.string.calendar_toolbar_title), true)
        //showCommonFragment(R.id.calendarFrameLayout, null, CommonFragmentType.CALENDAR, ticket)
    }

    private fun test() {
        val errorActivity = Intent(this@CalendarActivity, ErrorActivity::class.java)
            .apply {
                putExtra(EXTRA_INTENT, intent)
                putExtra(EXTRA_ERROR_TEXT, "aaa")
            }
        startActivity(errorActivity)
    }

//    fun showError(type: CommonFragmentType, errorMsg: String?) {
//        when (type) {
//            CommonFragmentType.NETWORK_FAIL -> {
//                val bundle = Bundle()
//                bundle.putInt("11", 1)
//                val abc: NetworkFailFragment = NetworkFailFragment.newInstance()
//                abc.arguments = bundle
//
//                //showCommonFragment(R.id.calendarFrameLayout, null, CommonFragmentType.NETWORK_FAIL)
//            }
//            CommonFragmentType.SERVER_ERROR -> {
//                showCommonFragment(R.id.calendarFrameLayout, errorMsg, CommonFragmentType.SERVER_ERROR)
//            }
//            else -> {
//
//            }
//        }
//    }
}