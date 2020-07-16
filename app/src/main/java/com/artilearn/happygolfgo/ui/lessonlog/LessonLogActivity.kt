package com.artilearn.happygolfgo.ui.lessonlog

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityLessonLogBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.util.MyApplication
import kotlinx.android.synthetic.main.activity_lesson_log.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class LessonLogActivity : BaseActivity<ActivityLessonLogBinding>(R.layout.activity_lesson_log) {
    private val TAG = javaClass.simpleName

    private lateinit var adapter: LessonLogAdapter
    private lateinit var myApplication: MyApplication
    private val viewModel: LessonLogViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return LessonLogViewModel(
                    myApplication.lessonLogRepository
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
        initViewModelObserve()
        initView()
        initAdapter()
    }

    private fun inject() {
        myApplication = application as MyApplication
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            networkFail.observe(this@LessonLogActivity, Observer {
                showToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            serverError.observe(this@LessonLogActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
        }
    }

    private fun initView() {
        initToolbar(lessonlog_toolbar.toolBar, getString(R.string.lessonlog_toolbar_title), true)
    }

    private fun initAdapter() {
        adapter = LessonLogAdapter { log ->
            showToast(ToastType.SUCCESS, "$log")
        }

        binding.recyclerView.adapter = adapter
    }
}