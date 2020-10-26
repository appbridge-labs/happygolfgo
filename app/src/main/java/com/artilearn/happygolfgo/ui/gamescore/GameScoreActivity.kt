package com.artilearn.happygolfgo.ui.gamescore

import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityGameScoreBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameScoreActivity : BaseActivity<ActivityGameScoreBinding>(R.layout.activity_game_score) {

    private val viewModel: GameScoreViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}