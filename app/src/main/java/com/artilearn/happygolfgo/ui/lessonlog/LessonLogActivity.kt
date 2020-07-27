package com.artilearn.happygolfgo.ui.lessonlog

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.LESSON_LOG
import com.artilearn.happygolfgo.data.LessonLog
import com.artilearn.happygolfgo.databinding.ActivityLessonLogBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.log.LogActivity
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
            goLog(log)
        }

        binding.recyclerView.adapter = adapter
    }

    private fun goLog(log: LessonLog) {
        val logIntent = Intent(this, LogActivity::class.java)
            .apply {
                putExtra(LESSON_LOG, log)
            }
        startActivity(logIntent)
    }
}