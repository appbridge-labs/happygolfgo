package com.artilearn.happygolfgo.ui.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityCalendarBinding
import com.artilearn.happygolfgo.util.MyApplication
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class CalendarActivity : BaseActivity<ActivityCalendarBinding>(R.layout.activity_calendar) {
    private val TAG = javaClass.simpleName

    private lateinit var myApplication: MyApplication
    private val viewModel: CalendarViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CalendarViewModel(

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

        }
    }

    private fun initView() {
        initToolbar(calendar_toolbar.toolBar, getString(R.string.calendar_toolbar_title), true)
    }
}