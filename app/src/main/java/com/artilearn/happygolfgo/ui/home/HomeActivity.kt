package com.artilearn.happygolfgo.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityHomeBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.util.MyApplication

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    private val TAG = javaClass.simpleName

    private lateinit var adapter: HomeAdapter
    private lateinit var myApplication: MyApplication
    private val viewModel: HomeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeViewModel(
                    myApplication.homeRepository
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
        ticketRefresh()
    }

    private fun inject() {
        myApplication = application as MyApplication
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            serverError.observe(this@HomeActivity, Observer {

            })
            networkFail.observe(this@HomeActivity, Observer {

            })
            networkDelay.observe(this@HomeActivity, Observer {

            })
        }
    }

    private fun initView() {
        initBottomView(binding.includeBottomView, getString(R.string.home_bottomview_title), BottimViewType.DEFAULT_TYPE)
    }

    private fun initAdapter() {
        adapter = HomeAdapter { ticket ->
            Toast.makeText(this, "$ticket", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerView.adapter = adapter
    }

    private fun ticketRefresh() {
        with(binding.swipeRefresh) {
            setOnRefreshListener {
                viewModel.requestTicket()
                isRefreshing = false
            }
            setColorSchemeColors(getColor(R.color.color_dark_green))
        }
    }

    override fun bottomAction() {
        super.bottomAction()

    }


}