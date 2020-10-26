package com.artilearn.happygolfgo.ui.gamerank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityGameRankBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameRankActivity : BaseActivity<ActivityGameRankBinding>(R.layout.activity_game_rank) {

    //private lateinit var pagerAdapter: GameRankPagerAdapter
    private val viewModel: GameRankViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initPagerAdapter()
    }

    private fun initView() {
        initToolbar(binding.rankToolbar.toolbar, getString(R.string.game_rank_toolbar_title), true)
    }

    private fun initPagerAdapter() {
        with(binding) {
            viewpager.adapter = GameRankPagerAdapter(this@GameRankActivity)

            TabLayoutMediator(tabLayout, viewpager) { tab, position ->
                tab.text = getTabTitle(position)
            }.attach()
        }
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            ALL_GROUP -> getString(R.string.game_rank_tab_title_allgroup)
            MY_GROUP -> getString(R.string.game_rank_tab_title_mygroup)
            else -> throw IndexOutOfBoundsException()
        }
    }

}