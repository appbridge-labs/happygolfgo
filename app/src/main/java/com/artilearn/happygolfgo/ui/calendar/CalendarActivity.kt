package com.artilearn.happygolfgo.ui.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.TICKET
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.ActivityCalendarBinding
import com.artilearn.happygolfgo.enummodel.SelectDateType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.util.MyApplication
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class CalendarActivity : BaseActivity<ActivityCalendarBinding>(R.layout.activity_calendar) {
    private val TAG = javaClass.simpleName

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
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            networkFail.observe(this@CalendarActivity, Observer {
                showToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            serverError.observe(this@CalendarActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
        }
    }

    private fun initView() {
        initToolbar(calendar_toolbar.toolBar, getString(R.string.calendar_toolbar_title), true)

        ticket = intent.getParcelableExtra(TICKET)
        ticket?.let {
            viewModel.ticketId = it.id
            viewModel.selectDate(SelectDateType.PLATE)
        }
    }
}