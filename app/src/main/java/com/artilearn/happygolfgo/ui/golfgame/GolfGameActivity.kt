package com.artilearn.happygolfgo.ui.golfgame

import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityGolfGameBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class GolfGameActivity : BaseActivity<ActivityGolfGameBinding>(R.layout.activity_golf_game) {

    private val viewModel: GolfGameViewModel by viewModel()
    private lateinit var adapter: GolfGameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        //initAdapter()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initAdapter() {
        adapter = GolfGameAdapter()

        binding.recyclerView.adapter = adapter
    }

}