package com.artilearn.happygolfgo.ui.home.gamecenter

import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.util.DisplayMetrics
import android.util.Log
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentGamecenterBinding
import com.artilearn.happygolfgo.ui.gamerank.GameRankActivity
import com.artilearn.happygolfgo.ui.gamescore.GameScoreActivity
import com.artilearn.happygolfgo.util.LabelCustomFormatter
import com.artilearn.happygolfgo.util.ScoreCustomFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.fragment_gamecenter.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class GameCenterFragment :
    BaseFragment<FragmentGamecenterBinding, GameCenterViewModel>(R.layout.fragment_gamecenter) {
    private val TAG = javaClass.simpleName
    override val viewModel: GameCenterViewModel by viewModel()

    override fun init() {

        initBinding()
        initViewModelObserving()

        replaceBarDataSetting()
        replaceSetBarData()
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
                    isEnabled = true
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

                animateY(2500)
                animateX(2000)
            }
        }
    }

    private fun replaceSetBarData() {
        val values = mutableListOf<BarEntry>()
        val colorList = listOf(
            Color.rgb(192, 255, 140),
            Color.rgb(255, 247, 97),
            Color.rgb(79, 208, 45),
            Color.rgb(140, 234, 255),
            Color.rgb(255, 140, 157)
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

    private fun goGameRank() {
        val rankIntent = Intent(activity, GameRankActivity::class.java)
        startActivity(rankIntent)
    }

    private fun goGameScore() {
        val scoreIntent = Intent(activity, GameScoreActivity::class.java)
        startActivity(scoreIntent)
    }

}