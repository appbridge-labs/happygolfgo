package com.artilearn.happygolfgo.ui.gamescore

import android.os.Bundle
import android.util.Log
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityGameScoreBinding
import com.github.mikephil.charting.charts.LineChart
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameScoreActivity : BaseActivity<ActivityGameScoreBinding>(R.layout.activity_game_score) {

    private val viewModel: GameScoreViewModel by viewModel()
    private lateinit var adapter: GameScoreAdapter
    private var list: ArrayList<GameScoreTestModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAdapter()
        setRandomData()
    }

    private fun initAdapter() {
        adapter = GameScoreAdapter(list)

        binding.recyclerView.adapter = adapter
    }

    private fun setRandomData() {
        list = arrayListOf()
        val data1 = GameScoreTestModel("드라이버")
        val data2 = GameScoreTestModel("우드")
        val data3 = GameScoreTestModel("아이언")
        val data4 = GameScoreTestModel("숏 아이언/웻지")
        val data5 = GameScoreTestModel("퍼팅")

        list.add(data1)
        list.add(data2)
        list.add(data3)
        list.add(data4)
        list.add(data5)

        adapter.setItems(list)
    }
}