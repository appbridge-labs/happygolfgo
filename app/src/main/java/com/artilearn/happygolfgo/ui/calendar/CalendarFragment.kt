package com.artilearn.happygolfgo.ui.calendar

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.TICKET
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.FragmentCalendarBinding
import com.artilearn.happygolfgo.ui.common.NetworkFailFragment
import com.artilearn.happygolfgo.ui.common.ServerErrorFragment
import com.artilearn.happygolfgo.util.MyApplication

class CalendarFragment
    : BaseFragment<FragmentCalendarBinding, CalendarFragmentViewModel>(R.layout.fragment_calendar) {
    private val TAG = javaClass.simpleName

    companion object {

        fun newInstance(ticket: Ticket): CalendarFragment {
            val args = Bundle()
            args.putParcelable(TICKET, ticket)
            val fragment = CalendarFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var ticket: Ticket? = null
    private lateinit var myApplication: MyApplication
    override val viewModel: CalendarFragmentViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CalendarFragmentViewModel(
                    myApplication.calendarRepository
                ) as T
            }
        }
    }

    override fun init() {
        myApplication = activity?.application as MyApplication
        binding.vm = viewModel

        ticket = arguments?.getParcelable(TICKET)
        ticket?.let {
            viewModel.ticketId = it.id
            viewModel.selectDate(it.type)
        }

        initViewModelObserve()
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            serverError.observe(this@CalendarFragment, Observer {
                showServerError(it)
            })
            networkFail.observe(this@CalendarFragment, Observer {
                showNetworkFail()
            })
            eventDayList.observe(this@CalendarFragment, Observer {
                binding.calendarview.setEvents(it)
            })
            availableMinDate.observe(this@CalendarFragment, Observer {
                binding.calendarview.setMinimumDate(it)
            })
            availableMaxDate.observe(this@CalendarFragment, Observer {
                binding.calendarview.setMaximumDate(it)
            })
        }
    }

    //todo 홈프래그먼트에서 인터넷이 끊긴 상태에서 티켓을 선택해서 캘린더로 들어오면 바로 네트워크 오류 프래그먼트가 뜬다...
    //todo base쪽 확인 후 layoutId, fragment 어떻게 나눌건지 생각 해봐야됨
    private fun showServerError(msg: String) {
        val serverError = ServerErrorFragment.newInstance(msg)
        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.calendarFrameLayout, serverError)
            .commit()
    }

    private fun showNetworkFail() {
        val networkFail = NetworkFailFragment.newInstance()
        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.calendarFrameLayout, networkFail)
            .commit()
    }

}