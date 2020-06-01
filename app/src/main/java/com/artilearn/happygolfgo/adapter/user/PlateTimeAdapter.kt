package com.artilearn.happygolfgo.adapter.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.models.PlateTime
import com.artilearn.happygolfgo.util.convertTime
import java.util.*

class PlateTimeAdapter : RecyclerView.Adapter<PlateTimeAdapter.PlateViewHolder>() {

    private val list: ArrayList<PlateTime> = ArrayList()
    private lateinit var listener: (PlateTime) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_time_selection, parent, false
        )

        val vh = PlateViewHolder(view)

        view.setOnClickListener {
            listener(list[vh.adapterPosition])
        }

        return vh
    }

    override fun onBindViewHolder(holder: PlateViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    class PlateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val startTimes = itemView.findViewById<TextView>(R.id.tv_startTime)
        val endTimes = itemView.findViewById<TextView>(R.id.tv_lastTime)
        val all = itemView.findViewById<TextView>(R.id.tv_all)
        val detail = itemView.findViewById<TextView>(R.id.tv_detail)

        fun bind(item: PlateTime) {
            val start = convertTime(item.startDate).trim().split(":")
            val end = convertTime(item.endDate).trim().split(":")

            startTimes.text = start[3] + ":" + start[4]
            endTimes.text = "~ ${end[3]}:${end[4]}"
            all.text = "총 ${item.totalCount}석"
            detail.text = "잔여 ${item.availableCount}석"

            when (item.availableCount) {
                0 -> {
                    startTimes.setTextColor(
                        ContextCompat.getColor(
                            startTimes.context,
                            R.color.color_dark_red
                        )
                    )
                    endTimes.setTextColor(
                        ContextCompat.getColor(
                            startTimes.context,
                            R.color.color_dark_red
                        )
                    )
                    all.setTextColor(
                        ContextCompat.getColor(
                            startTimes.context,
                            R.color.color_dark_red
                        )
                    )
                    detail.setTextColor(
                        ContextCompat.getColor(
                            startTimes.context,
                            R.color.color_dark_red
                        )
                    )
                }
                else -> {
                    startTimes.setTextColor(
                        ContextCompat.getColor(
                            startTimes.context,
                            R.color.color_black
                        )
                    )
                    endTimes.setTextColor(
                        ContextCompat.getColor(
                            startTimes.context,
                            R.color.color_black
                        )
                    )
                    all.setTextColor(
                        ContextCompat.getColor(
                            startTimes.context,
                            R.color.color_black
                        )
                    )
                    detail.setTextColor(
                        ContextCompat.getColor(
                            startTimes.context,
                            R.color.color_black
                        )
                    )
                }
            }

        }
    }

    fun setPlate(item: List<PlateTime>) {
        this.list.addAll(item)
        notifyDataSetChanged()
    }

    fun isPlateClear() {
        this.list.clear()
    }

    fun plateOnClick(listener: (PlateTime) -> Unit) {
        this.listener = listener
    }

}
