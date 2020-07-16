package com.artilearn.happygolfgo.ui.lessonlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityLessonListBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.util.MyApplication
import kotlinx.android.synthetic.main.activity_lesson_list.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class LessonListActivity : BaseActivity<ActivityLessonListBinding>(R.layout.activity_lesson_list) {
    private val TAG = javaClass.simpleName

    private lateinit var adapter: LessonAdapter
    private lateinit var myApplication: MyApplication
    private val viewModel: LessonListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return LessonListViewModel(

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
        adapter = LessonAdapter { log ->
            showToast(ToastType.SUCCESS, "$log")
        }

        binding.recyclerView.adapter = adapter
    }
}