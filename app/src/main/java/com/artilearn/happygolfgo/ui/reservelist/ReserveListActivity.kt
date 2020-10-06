package com.artilearn.happygolfgo.ui.reservelist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityReserveListBinding
import com.artilearn.happygolfgo.util.MyApplication
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ReserveListActivity :
    BaseActivity<ActivityReserveListBinding>(R.layout.activity_reserve_list) {

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
        initTabListener()
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
        initToolbar(binding.reserveListToolbar.toolbar, getString(R.string.reserve_list_toolbar_title), true)
    }

    private fun initTabListener() {
        with(binding) {
            reserveListTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) = Unit
                override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> tab.parent?.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                this@ReserveListActivity,
                                R.color.color_green
                            )
                        )
                        1 -> tab.parent?.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                this@ReserveListActivity,
                                R.color.color_reservation_lesson_text_month
                            )
                        )
                    }
                }
            })
        }
    }

}