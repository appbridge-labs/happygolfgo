package com.artilearn.happygolfgo.ui.log

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityLogBinding
import com.artilearn.happygolfgo.util.MyApplication
import kotlinx.android.synthetic.main.activity_log.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class LogActivity : BaseActivity<ActivityLogBinding>(R.layout.activity_log) {

    private lateinit var myApplication: MyApplication
    private val viewModel: LogViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return LogViewModel(

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
        initToolbar(log_toolbar.toolBar, getString(R.string.log_toolbar_title), true)
    }
}