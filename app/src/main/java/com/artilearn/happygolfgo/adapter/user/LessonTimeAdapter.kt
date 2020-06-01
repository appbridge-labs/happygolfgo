package com.artilearn.happygolfgo.adapter.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.models.LessonTime
import com.artilearn.happygolfgo.util.convertTime
import java.util.*


class LessonTimeAdapter : RecyclerView.Adapter<LessonTimeAdapter.LessonViewHolder>() {

    private val list: ArrayList<LessonTime> = ArrayList()
    private lateinit var callback: (LessonTime) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_time_selection, parent, false
        )

        val vh = LessonViewHolder(view)

        view.setOnClickListener {
            callback(list[vh.adapterPosition])
        }

        return vh
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        list[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount() = list.size

    class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val startTimes = itemView.findViewById<TextView>(R.id.tv_startTime)
        val endTimes = itemView.findViewById<TextView>(R.id.tv_lastTime)
        val all = itemView.findViewById<TextView>(R.id.tv_all)
        val detail = itemView.findViewById<TextView>(R.id.tv_detail)

        fun bind(item: LessonTime) {
            val start = convertTime(item.startDate).trim().split(":")
            val end = convertTime(item.endDate).trim().split(":")

            startTimes.text = start[3] + ":" + start[4]
            endTimes.text = "~ ${end[3]}:${end[4]}"

            all.text = "상태"

            when (item.status) {
                0 -> {
                    detail.text = "예약불가"
                    detail.setTextColor(
                        ContextCompat.getColor(
                            detail.context,
                            R.color.color_dark_red
                        )
                    )
                }
                1 -> {
                    detail.text = "예약가능"
                    detail.setTextColor(
                        ContextCompat.getColor(
                            detail.context,
                            R.color.color_black
                        )
                    )
                }
            }
        }
    }

    fun setLesson(item: List<LessonTime>) {
        this.list.addAll(item)
        notifyDataSetChanged()
    }

    fun isLessonClear() {
        this.list.clear()
    }

    fun setItemClickListener(callback: (LessonTime) -> Unit) {
        this.callback = callback
    }

}