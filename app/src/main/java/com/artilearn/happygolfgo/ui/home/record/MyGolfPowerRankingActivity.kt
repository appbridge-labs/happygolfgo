package com.artilearn.happygolfgo.ui.home.record

import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.MY_GOLF_POWER_ADAPTER_INDEX
import com.artilearn.happygolfgo.databinding.ActivityMyGolfPowerRankingBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyGolfPowerRankingActivity :
    BaseActivity<ActivityMyGolfPowerRankingBinding>(R.layout.activity_my_golf_power_ranking) {

    private val viewModel: MyGolfPowerRankingViewModel by viewModel()
    private lateinit var rankingPagerAdapter: MyGolfPowerRankingAdapter
    private val golfPowerAdapterIndex by lazy {
         intent.getIntExtra(MY_GOLF_POWER_ADAPTER_INDEX, -1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initView()
        initTabListener()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
                rankingPagerAdapter = MyGolfPowerRankingAdapter(this@MyGolfPowerRankingActivity, golfPowerAdapterIndex)

                binding.viewpager.adapter = rankingPagerAdapter
                TabLayoutMediator(binding.rankingListTablayout, binding.viewpager,
                    TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                        when (position) {
                            0 -> tab.text = getString(R.string.my_golf_power_ranking_branch_title)
                            1 -> tab.text = getString(R.string.my_golf_power_ranking_by_subject_title)
                            2 -> tab.text = getString(R.string.my_golf_power_ranking_all_branch_title)
                        }
                    }).attach()
        }
    }

    private fun initView() {
        binding.myGolfPowerRakingTopSection.setOnClickListener {
            showToast(ToastType.SUCCESS, "준비 중입니다.")
        }
    }

    private fun initTabListener() {
    }
}
