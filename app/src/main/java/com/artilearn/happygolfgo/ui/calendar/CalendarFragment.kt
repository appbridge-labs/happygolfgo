package com.artilearn.happygolfgo.ui.calendar

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentCalendarBinding
import com.artilearn.happygolfgo.util.MyApplication

class CalendarFragment
    : BaseFragment<FragmentCalendarBinding, CalendarFragmentViewModel>(R.layout.fragment_calendar) {

    companion object {

        fun newInstance(): CalendarFragment {
            return CalendarFragment()
        }
    }

    private lateinit var myApplication: MyApplication
    override val viewModel: CalendarFragmentViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CalendarFragmentViewModel(

                ) as T
            }
        }
    }

    override fun init() {
        myApplication = activity?.application as MyApplication
        binding.vm = viewModel

        initViewModelObserve()
    }

    private fun initViewModelObserve() {
        with(viewModel) {

        }
    }
}