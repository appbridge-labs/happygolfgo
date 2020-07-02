package com.artilearn.happygolfgo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.models.LessonReservation
import com.artilearn.happygolfgo.util.convertTime

class LessonReservationAdapter : RecyclerView.Adapter<LessonReservationAdapter.ItemViewHolder>() {

    private val lessonList: ArrayList<LessonReservation> = ArrayList()
    var listener: LessonClick? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_reservation_card_view, parent, false
        )

        return ItemViewHolder(
            view
        ).also {
            it.itemView.setOnClickListener { position ->
                val curretItem =
                    lessonList.getOrNull(it.adapterPosition) ?: return@setOnClickListener
                listener?.lesson(curretItem)
            }
        }
    }

    override fun getItemCount() = lessonList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(lessonList[position])
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.text_view_title_reservation_card)
        val date = itemView.findViewById<TextView>(R.id.text_view_date_reservation_card)
        val time = itemView.findViewById<TextView>(R.id.text_view_time_reservation_card)
        val check = itemView.findViewById<TextView>(R.id.text_view_status_reservation_card)

        fun bind(item: LessonReservation) {
            val start = convertTime(item.startDate).trim().split(":")
            val end = convertTime(item.endDate).trim().split(":")

            title.text = item.name
            date.text = start[1] + "." + start[2]
            time.text = start[3] + ":" + start[4] + " ~ " + end[3] + ":" + end[4]
            check.text = "예약완료"
        }
    }

    fun setItem(item: List<LessonReservation>) {
        this.lessonList.addAll(item)
        notifyDataSetChanged()
    }

    fun itemClear() {
        this.lessonList.clear()
        notifyDataSetChanged()
    }

    interface LessonClick {
        fun lesson(lesson: LessonReservation)
    }
}

