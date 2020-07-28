package com.artilearn.happygolfgo.ui.reservelist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.data.ReserveList
import com.artilearn.happygolfgo.databinding.ActivityReserveListBinding
import com.artilearn.happygolfgo.util.MyApplication
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_reserve_list.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class ReserveListActivity :
    BaseActivity<ActivityReserveListBinding>(R.layout.activity_reserve_list) {
    private val TAG = javaClass.simpleName

    private lateinit var pagerAdaper: ReservePagerListAdapter
    private lateinit var myApplication: MyApplication
    private val viewModel: ReserveListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ReserveListViewModel(
                    myApplication.reserveListRepository
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
            reservationList.observe(this@ReserveListActivity, Observer {
                pagerAdaper = ReservePagerListAdapter(this@ReserveListActivity, it)

                binding.viewpager.adapter = pagerAdaper
                TabLayoutMediator(binding.reserveListTablayout, binding.viewpager,
                    TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                        when (position) {
                            0 -> tab.text = getString(R.string.reserve_list_plate_title)
                            1 -> tab.text = getString(R.string.reserve_list_lesson_title)
                        }
                    }).attach()
            })
            networkFail.observe(this@ReserveListActivity, Observer {
                showNetworkFail()
            })
            serverError.observe(this@ReserveListActivity, Observer {
                showError(it)
            })
        }
    }

    private fun initView() {
        initToolbar(reserve_list_toolbar.toolBar, getString(R.string.reserve_list_toolbar_title), true)
    }

}