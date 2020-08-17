package com.artilearn.happygolfgo.ui.alram

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityAlramBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.util.MyApplication

class AlramActivity : BaseActivity<ActivityAlramBinding>(R.layout.activity_alram) {

    private lateinit var adapter: AlramAdapter
    private lateinit var myApplication: MyApplication
    private val viewModel: AlramViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AlramViewModel(
                    myApplication.alramRepository
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
            serverError.observe(this@AlramActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            networkFail.observe(this@AlramActivity, Observer {
                showToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
        }
    }

    private fun initView() {
        initToolbar(binding.alramToolbar.toolbar, getString(R.string.alram_toolbar_title), true)
    }

    private fun initAdapter() {
        adapter = AlramAdapter { alram ->

        }

        binding.recyclerview.adapter = adapter
    }
}