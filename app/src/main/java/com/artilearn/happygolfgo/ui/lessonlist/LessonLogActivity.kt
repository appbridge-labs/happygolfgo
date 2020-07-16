package com.artilearn.happygolfgo.ui.lessonlist

import android.os.Bundle
import androidx.activity.viewModels
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

        }
    }

    private fun initView() {
        initToolbar(lessonlist_toolbar.toolBar, "레슨 일지", true)
    }

    private fun initAdapter() {
        adapter = LessonLogAdapter { log ->
            showToast(ToastType.SUCCESS, "$log")
        }

        binding.recyclerView.adapter = adapter
    }
}