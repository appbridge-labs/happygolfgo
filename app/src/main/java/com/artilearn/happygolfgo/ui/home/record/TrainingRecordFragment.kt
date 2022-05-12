package com.artilearn.happygolfgo.ui.home.record

import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.SWING_ROUND_DETAIL_ID
import com.artilearn.happygolfgo.databinding.FragmentTrainingRecordBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.golfgame.GolfGameActivity
import com.artilearn.happygolfgo.ui.golfrank.GolfRankActivity
import com.artilearn.happygolfgo.ui.home.record.model.TRSummaryGolfPowerRecordModel
import com.artilearn.happygolfgo.ui.home.record.model.TRSummaryWeightModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


//comments: vc45
class TrainingRecordFragment :
    BaseFragment<FragmentTrainingRecordBinding,
            TrainingRecordViewModel>(R.layout.fragment_training_record) {
    private val TAG = javaClass.simpleName
    override val viewModel: TrainingRecordViewModel by viewModel()

    var clickable = true;
    private val chartNoDataTextSize = 120.0f

    override fun init() {
        initBinding()
        initViewModelObserving()
        initAdapter()
        initCharts()
        initOnClickListener()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
           summaryTime.observe( this@TrainingRecordFragment, Observer { summaryTime ->
               binding.summaryTime = summaryTime
           })

            summaryWeight.observe( this@TrainingRecordFragment, Observer { summaryWeight ->
                binding.summaryWeight = summaryWeight
                setSummaryWeightChartData(summaryWeight)
            })


            summaryGolfPower.observe( this@TrainingRecordFragment, Observer { summaryGoldPower ->
                binding.summaryGolfPower = summaryGoldPower
            })

            golfPowerRecords.observe( this@TrainingRecordFragment, Observer { golfPowerRecords ->
                setGolfPowerChartData(golfPowerRecords)
            })


            summaryRound.observe( this@TrainingRecordFragment, Observer { summaryRound ->
                binding.summaryRound = summaryRound
            })

            networkFail.observe(this@TrainingRecordFragment, Observer {
                showFragmentToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            serverError.observe(this@TrainingRecordFragment, Observer {
                showFragmentToast(ToastType.ERROR, it)
            })
            appRefresh.observe(this@TrainingRecordFragment, Observer {
                goRefreshFromFragment()
            })
        }
    }

    private fun initAdapter() {

    }

    private fun initOnClickListener() {
       with(binding)  {
           swingVideoSectionLayoutLl.setOnClickListener { goSwingVideoList() }
           roundSectionLayoutBestScoreLl.setOnClickListener { goRoundDetail( summaryRound?.bestGmId ?: 0 ) }
           roundSectionLayoutLastScoreLl.setOnClickListener { goRoundDetail( summaryRound?.lastGmId ?: 0) }
       }
    }

    private fun initCharts() {
        initWeightChart();
        initGolfPowerChart();
    }

    private fun initWeightChart() {
        with(binding) {
            weightSectionLayoutChart.run {
                setUsePercentValues(true)
                description.text = ""
                //hollow pie chart
                isDrawHoleEnabled = false
                setTouchEnabled(false)
                setDrawEntryLabels(true)
                setNoDataText("데이타가 없습니다.")
                getPaint(Chart.PAINT_INFO).textSize = chartNoDataTextSize
                setUsePercentValues(true)
                isRotationEnabled = false
                setDrawEntryLabels(true)
                legend.orientation = Legend.LegendOrientation.VERTICAL
                legend.isWordWrapEnabled = true
                animateY(1000)
                animateX(700)
            }
        }
    }

    // 바차트에서 파이차트로 변경
    private fun setSummaryWeightChartData(item: TRSummaryWeightModel) {
        with(binding) {
            weightSectionLayoutChart.run {
                setUsePercentValues(true)
                setDrawEntryLabels(false)
                setDrawMarkers(true)
                isDrawHoleEnabled = false
                legend.isEnabled = false
                val dataEntries = ArrayList<PieEntry>()

                for (i in 0..4) {
                    when (i) {
                        0 -> dataEntries.add(PieEntry(item.drange,"장타"))
                        1 -> dataEntries.add(PieEntry(item.appr,"어프로치"))
                        2 -> dataEntries.add(PieEntry(item.putt,"퍼팅"))
                        3 -> dataEntries.add(PieEntry(item.gpower,"골프력"))
                        4 -> dataEntries.add(PieEntry(item.stroke,"게임"))
                    }
                }

                for (i in 0..4) {
                    when (i) {
                        0 -> binding.trainingRecordWeightSummaryDrange.text = String.format("%.1f%%", item.drange)
                        1 -> binding.trainingRecordWeightSummaryAppr.text = String.format("%.1f%%", item.appr)
                        2 -> binding.trainingRecordWeightSummaryPutt.text = String.format("%.1f%%", item.putt)
                        3 -> binding.trainingRecordWeightSummaryGpower.text = String.format("%.1f%%", item.gpower)
                        4 -> binding.trainingRecordWeightSummaryStroke.text = String.format("%.1f%%", item.stroke)
                    }
                }

                val colors: ArrayList<Int> = ArrayList()

                colors.add(Color.parseColor("#7FAFCF"))
                colors.add(Color.parseColor("#3F7AA4"))
                colors.add(Color.parseColor("#5B5D97"))
                colors.add(Color.parseColor("#FEC74E"))
                colors.add(Color.parseColor("#F06A20"))


                val dataSet = PieDataSet(dataEntries, "")
                val data = PieData(dataSet)

                dataSet.colors = colors
                dataSet.setDrawValues(false)
                this.data = data
                animateY(1400, Easing.EaseInOutQuad)
                invalidate()
            }
        }
    }

    private fun initGolfPowerChart() {
        with(binding) {
            golfPowerSectionLayoutChart.run {
                setDrawBarShadow(false)
                setTouchEnabled(false)
                setDrawValueAboveBar(false)
                setPinchZoom(false)
                setDrawGridBackground(false)
                setNoDataText("데이타가 없습니다.")
                getPaint(Chart.PAINT_INFO).textSize = chartNoDataTextSize
                description.isEnabled = false
                legend.isEnabled = false
                setVisibleXRangeMaximum(7f)

                xAxis.run {
                    isEnabled = false
                    setDrawGridLines(true)
                    granularity = 1f
                    setDrawAxisLine(true)
                    setAxisLineColor(Color.BLUE);
                    textSize = 12f
                }

                axisLeft.run {
                    isEnabled = true
                    axisMinimum = 0f
                    axisMaximum = 100f
                    textSize = 0f;
                    textColor = Color.WHITE
                }
                axisRight.run {
                    isEnabled = true
                    axisMinimum = 0f
                    axisMaximum = 100f
                    textSize = 0f;
                    textColor = Color.WHITE
                }

                animateY(1000)
                animateX(700)
            }
        }
    }

    private fun setGolfPowerChartData(items: List<TRSummaryGolfPowerRecordModel>) {

        val bestValues = mutableListOf<BarEntry>()
        val avgValues = mutableListOf<BarEntry>()

        val bestColorList = listOf(
            Color.parseColor("#F06A20")
        )

        val avgColorList = listOf(
            Color.parseColor("#7FAFCF"),
        )

        items.mapIndexed { index, item ->
            bestValues.add(BarEntry(index.toFloat(), item.best))
        }

        items.mapIndexed { index, item ->
            avgValues.add(BarEntry(index.toFloat(), item.avg))
        }

        val bestDataSet = BarDataSet(bestValues, "내용없음")
            .apply {
                setDrawIcons(false)
                colors = bestColorList
                valueTextColor = Color.WHITE
                valueFormatter= object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val score = value.toString().split(".")
                        return score[0]
                    }
                }
            }

        val avgDataSet = BarDataSet(avgValues, "내용없음")
            .apply {
                setDrawIcons(false)
                colors = avgColorList
                valueTextColor = Color.WHITE
                valueFormatter= object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val score = value.toString().split(".")
                        return score[0]
                    }
                }
            }
        val listDataSet= mutableListOf<IBarDataSet>()

        listDataSet.add(avgDataSet)
        listDataSet.add(bestDataSet)


        val data = BarData(listDataSet)
            .apply {
                setValueTextSize(10f)
                barWidth = 0.4f
            }

        val groupSpace = 0.2f
        val barSpace = 0f
        var barWidth = 0.4f
        // (barSpace + barWidth) * 2 + groupSpace = 1
        // (barSpace + barWidth) * 2 + groupSpace = 1
        data.setBarWidth(barWidth)
        // so that the entire chart is shown when scrolled from right to left
        //xAxis.setAxisMaximum(labels.length - 1.1f)
        binding.golfPowerSectionLayoutChart.data = data; //original

        binding.golfPowerSectionLayoutChart.groupBars(-0.5f, groupSpace, barSpace);
        binding.golfPowerSectionLayoutChart.setScaleEnabled(false)
        binding.golfPowerSectionLayoutChart.setVisibleXRangeMaximum(7f)

        binding.golfPowerSectionLayoutChart.animateY(2000, Easing.EaseInOutQuad)
        binding.golfPowerSectionLayoutChart.animateX(1000, Easing.EaseInOutQuad)
        binding.golfPowerSectionLayoutChart.invalidate()
    }

    private fun goGameRank() {
        val rankIntent = Intent(activity, GolfRankActivity::class.java)
        startActivity(rankIntent)
    }

    private fun goGameScore() {
        val scoreIntent = Intent(activity, GolfGameActivity::class.java)
        startActivity(scoreIntent)
    }

    private fun goRoundDetail(gmID:Int) {
        if (gmID < 1) {
            return ;
        }
        if (clickable) {
            clickable = false
            val roundDetailIntent =
                Intent(activity, TrainingRecordRoundDetailActivity::class.java)
                    .apply {
                        putExtra(SWING_ROUND_DETAIL_ID, gmID)
                    }
            startActivity(roundDetailIntent)
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    clickable = true;
                }
            }, 1000)
        }
    }

    private fun goSwingVideoList() {
        if (clickable) {
            clickable = false
            val swingVideoIntent =
                Intent(activity, TrainingRecordSwingVideoListActivity::class.java)
            startActivity(swingVideoIntent)
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    clickable = true;
                }
            }, 1000)
        }
    }
}
