package com.artilearn.happygolfgo.ui.home.gamecenter

import android.content.Intent
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentGamecenterBinding
import com.artilearn.happygolfgo.ui.gamerank.GameRankActivity
import com.artilearn.happygolfgo.ui.gamescore.GameScoreActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameCenterFragment :
    BaseFragment<FragmentGamecenterBinding, GameCenterViewModel>(R.layout.fragment_gamecenter) {

    override val viewModel: GameCenterViewModel by viewModel()

    override fun init() {

        initBinding()
        initViewModelObserving()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            button.observe(this@GameCenterFragment, Observer {
                when (button.value) {
                    GameCenterViewModel.ButtonType.RANK -> goGameRank()
                    GameCenterViewModel.ButtonType.GAME -> goGameScore()
                }
            })
        }
    }

    private fun goGameRank() {
        val rankIntent = Intent(activity, GameRankActivity::class.java)
        startActivity(rankIntent)
    }

    private fun goGameScore() {
        val scoreIntent = Intent(activity, GameScoreActivity::class.java)
        startActivity(scoreIntent)
    }

}