package com.artilearn.happygolfgo.ui.home.gamecenter

import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentGamecenterBinding
import com.artilearn.happygolfgo.ui.gamerank.GameRankActivity
import com.artilearn.happygolfgo.ui.gamerank.GameRankTestModel
import com.artilearn.happygolfgo.ui.gamescore.GameScoreActivity
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class GameCenterFragment :
    BaseFragment<FragmentGamecenterBinding, GameCenterViewModel>(R.layout.fragment_gamecenter) {
    private val TAG = javaClass.simpleName
    override val viewModel: GameCenterViewModel by viewModel()
    private lateinit var adapter: GameCenterAdapter
    private var list: ArrayList<GameRankTestModel> = arrayListOf()

    override fun init() {

        initBinding()
        initViewModelObserving()
        initAdapter()

        replaceBarDataSetting()
        replaceSetBarData()
        setRandomData()
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
            user.observe(this@GameCenterFragment, Observer {
                binding.user = it
            })
        }
    }

    private fun replaceBarDataSetting() {
        with(binding) {
            barChart.run {
                setDrawBarShadow(true)
                setTouchEnabled(false)
                setDrawValueAboveBar(false)
                setPinchZoom(false)
                setDrawGridBackground(false)
                description.isEnabled = false
                legend.isEnabled = false
                setVisibleXRangeMaximum(5f)

                xAxis.run {
                    isEnabled = false
                    setDrawGridLines(false)
                    granularity = 1f
                    setDrawAxisLine(false)
                    textSize = 12f
                }

                axisLeft.run {
                    isEnabled = false
                }
                axisRight.run {
                    isEnabled = false
                }

                animateY(1500)
                animateX(1000)
            }
        }
    }

    private fun replaceSetBarData() {
        val values = mutableListOf<BarEntry>()
        val colorList = listOf(
            Color.rgb(204, 93, 232),
            Color.rgb(132, 94, 247),
            Color.rgb(92, 124, 250),
            Color.rgb(34, 184, 207),
            Color.rgb(81, 207, 102)
        )

        for (i in 6..10) {
            val index = i.toFloat()
            val random = Random().nextInt(101).toFloat()

            values.add(BarEntry(index, random))
        }

        val set = BarDataSet(values, "내용없음")
            .apply {
                setDrawIcons(false)
                setDrawValues(false)
                colors = colorList
                valueTextColor = Color.RED
            }

        val dataSets = mutableListOf<IBarDataSet>()
        dataSets.add(set)

        val data = BarData(dataSets)
            .apply {
                setValueTextSize(10f)
                barWidth = 0.5f
            }

        binding.barChart.data = data
    }

    private fun initAdapter() {
        adapter = GameCenterAdapter(list)

        binding.rvAllRank.adapter = adapter
    }

    private fun setRandomData() {
        list = arrayListOf()
        val data1 = GameRankTestModel(100, "행골나들이", 8.7f)
        val data2 = GameRankTestModel(101, "킹드라이버", 6.7f)
        val data3 = GameRankTestModel(102, "싱글진입하자", 4.7f)

        list.add(data1)
        list.add(data2)
        list.add(data3)

        adapter.setItems(list)
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