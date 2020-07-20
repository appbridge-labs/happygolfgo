package com.artilearn.happygolfgo.ui.calendar

//import android.app.Activity
//import android.content.Context
//import android.os.Bundle
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.artilearn.happygolfgo.R
//import com.artilearn.happygolfgo.base.BaseFragment
//import com.artilearn.happygolfgo.constants.TICKET
//import com.artilearn.happygolfgo.data.Ticket
//import com.artilearn.happygolfgo.databinding.FragmentCalendarBinding
//import com.artilearn.happygolfgo.enummodel.CommonFragmentType
//import com.artilearn.happygolfgo.util.MyApplication
//
//class CalendarFragment
//    : BaseFragment<FragmentCalendarBinding, CalendarFragmentViewModel>(R.layout.fragment_calendar) {
//    private val TAG = javaClass.simpleName
//
//    companion object {
//
//        fun newInstance(ticket: Ticket): CalendarFragment {
//            val args = Bundle()
//            args.putParcelable(TICKET, ticket)
//            val fragment = CalendarFragment()
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//    private var ticket: Ticket? = null
//    private lateinit var myApplication: MyApplication
//    override val viewModel: CalendarFragmentViewModel by viewModels {
//        object : ViewModelProvider.Factory {
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                return CalendarFragmentViewModel(
//                    myApplication.calendarRepository
//                ) as T
//            }
//        }
//    }
//
//    override fun init() {
//        myApplication = activity?.application as MyApplication
//        binding.vm = viewModel
//
//        ticket = arguments?.getParcelable(TICKET)
//        ticket?.let {
//            viewModel.ticketId = it.id
//            viewModel.selectDate(it.type)
//        }
//
//        initViewModelObserve()
//    }
//
//    private fun initViewModelObserve() {
//        with(viewModel) {
//            serverError.observe(this@CalendarFragment, Observer {
//                (activity as CalendarActivity).showError(CommonFragmentType.SERVER_ERROR, it)
//            })
//            networkFail.observe(this@CalendarFragment, Observer {
//                (activity as CalendarActivity).showError(CommonFragmentType.NETWORK_FAIL, null)
//            })
//            eventDayList.observe(this@CalendarFragment, Observer {
//                binding.calendarview.setEvents(it)
//            })
//            availableMinDate.observe(this@CalendarFragment, Observer {
//                binding.calendarview.setMinimumDate(it)
//            })
//            availableMaxDate.observe(this@CalendarFragment, Observer {
//                binding.calendarview.setMaximumDate(it)
//            })
//        }
//
//    }
//
//}