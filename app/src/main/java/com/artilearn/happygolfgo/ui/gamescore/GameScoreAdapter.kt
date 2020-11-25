package com.artilearn.happygolfgo.ui.gamescore

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.databinding.ItemGolfGameScoreBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*
import kotlin.collections.ArrayList


class GameScoreAdapter(private val items: ArrayList<GameScoreTestModel>) : RecyclerView.Adapter<GameScoreAdapter.GameScoreViewHolder>() {

    lateinit var binding: ItemGolfGameScoreBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameScoreViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_golf_game_score,
            parent,
            false
        )

        return GameScoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameScoreViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class GameScoreViewHolder(
        private val binding: ItemGolfGameScoreBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val lineChart = binding.lineChart

        fun bind(item: GameScoreTestModel) {
            binding.tvClub.text = item.club
            lineChart.run {
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

                data = setLineData()
                animateX(1000)
                animateY(2000)
            }

            binding.executePendingBindings()
        }

        private fun setLineData(): LineData {
            val sets = mutableListOf<ILineDataSet>()

            val test1 = mutableListOf<Entry>()

            for (i in 0..5) {
                val index = i.toFloat()
                val random = Random()
                val range = random.nextInt(101).toFloat()

                test1.add(Entry(index, range))
            }

            val abc = LineDataSet(test1, "라벨1")
            abc.run {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                circleRadius = 4f
                lineWidth = 1.8f
                cubicIntensity = 0.2f
                setDrawCircles(false)
                setDrawValues(false)
                setDrawHighlightIndicators(false)

                color = ColorTemplate.VORDIPLOM_COLORS[0]
            }

            sets.add(abc)

            val ld = LineData(sets)
            ld.setDrawValues(false)

            return ld
        }
    }

    fun setItems(items: List<GameScoreTestModel>) {
        clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
    }

}