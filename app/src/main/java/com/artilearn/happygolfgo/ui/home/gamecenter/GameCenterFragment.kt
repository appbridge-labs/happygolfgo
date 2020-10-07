package com.artilearn.happygolfgo.ui.home.gamecenter

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentGamecenterBinding
import com.artilearn.happygolfgo.ui.gamerank.GameRankActivity
import com.artilearn.happygolfgo.util.MyApplication

class GameCenterFragment :
    BaseFragment<FragmentGamecenterBinding, GameCenterViewModel>(R.layout.fragment_gamecenter) {

    private lateinit var myApplication: MyApplication

    override val viewModel: GameCenterViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return GameCenterViewModel() as T
            }
        }
    }

    override fun init() {

        inject()
        initViewModelObserving()
    }

    private fun inject() {
        myApplication = activity?.application as MyApplication
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            button.observe(this@GameCenterFragment, Observer {
                when (button.value) {
                    GameCenterViewModel.ButtonType.RANK -> goRank()
                    GameCenterViewModel.ButtonType.GAME -> ""
                }
            })
        }
    }

    private fun goRank() {
        val rankIntent = Intent(activity, GameRankActivity::class.java)
        startActivity(rankIntent)
    }

}