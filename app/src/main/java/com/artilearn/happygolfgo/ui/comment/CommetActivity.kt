package com.artilearn.happygolfgo.ui.comment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityCommetBinding
import com.artilearn.happygolfgo.util.MyApplication

class CommetActivity : BaseActivity<ActivityCommetBinding>(R.layout.activity_commet) {

    private lateinit var myApplication: MyApplication
    private val viewModel: CommentViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CommentViewModel(
                    myApplication.commentRepository
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
        initToolbar(binding.commentToolbar.toolBar, getString(R.string.log_toolbar_title), true)
    }
}