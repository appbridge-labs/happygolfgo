package com.artilearn.happygolfgo.util.databinding

import android.graphics.Color
import android.util.Log
import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.util.*

@BindingAdapter("score", "club")
fun LineChart.bindScore(score: List<Float>?, club: String) {
    this.run {
        setTouchEnabled(false)
        setPinchZoom(false)
        isDragEnabled = false
        setScaleEnabled(false)
        description.isEnabled = false
        setDrawGridBackground(false)
        legend.isEnabled = false
        setVisibleXRangeMaximum(5f)

        xAxis.run {
            isEnabled = false
        }

        axisLeft.run {
            axisMaximum = 100f
            axisMinimum = 0f
            textColor = Color.LTGRAY
        }

        axisRight.run {
            isEnabled = false
        }

        data = setLineData(score, club)

        animateY(1000)
        animateX(700)
    }
}

private fun setLineData(score: List<Float>?, club: String): LineData {
    val dataSets = mutableListOf<ILineDataSet>()
    val entry = mutableListOf<Entry>()

    for (i in 0..4) {
        if (score?.size != 0) {
            entry.add(Entry(i.toFloat(), score?.get(i) ?: 0f))
        } else {
            entry.add(Entry(i.toFloat(), 0f))
        }
    }

    val iLineDataSet = LineDataSet(entry, "내용없음")
    iLineDataSet.run {
        mode = LineDataSet.Mode.CUBIC_BEZIER
        circleRadius = 4f
        lineWidth = 1.8f
        cubicIntensity = 0.2f
        setDrawCircles(false)
        setDrawValues(false)
        setDrawHighlightIndicators(false)

        color = when (club) {
            "드라이버" -> Color.RED
            "우드" -> Color.BLACK
            "아이언" -> Color.LTGRAY
            "숏 아이언 / 웻지" -> Color.GREEN
            "퍼팅" -> Color.BLUE
            else -> throw IllegalArgumentException("Unknown Club")
        }
    }

    dataSets.add(iLineDataSet)

    return LineData(dataSets).apply {
        setDrawValues(false)
    }
}