package com.artilearn.happygolfgo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.models.PlateReservation
import com.artilearn.happygolfgo.util.convertTime

class PlateReservationAdapter : RecyclerView.Adapter<PlateReservationAdapter.ItemViewHolder>() {

    private val reserveList: ArrayList<PlateReservation> = ArrayList()
    var listener: ReserveClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_reservation_card_view, parent, false
        )

        return ItemViewHolder(
            view
        ).also {
            it.itemView.setOnClickListener { position ->
                val currentItem =
                    reserveList.getOrNull(it.adapterPosition) ?: return@setOnClickListener
                listener?.reserve(currentItem)
            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(reserveList[position])
    }

    override fun getItemCount() = reserveList.size

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.text_view_title_reservation_card)
        val date = itemView.findViewById<TextView>(R.id.text_view_date_reservation_card)
        val time = itemView.findViewById<TextView>(R.id.text_view_time_reservation_card)
        val check = itemView.findViewById<TextView>(R.id.text_view_status_reservation_card)

        fun bind(item: PlateReservation) {
            val start = convertTime(item.startDate).trim().split(":")
            val end = convertTime(item.endDate).trim().split(":")

            title.text = item.name
            date.text = start[1] + "." + start[2]
            time.text = start[3] + ":" + start[4] + " ~ " + end[3] + ":" + end[4]
            check.text = "예약완료"
        }
    }

    fun setItem(item: List<PlateReservation>) {
        this.reserveList.addAll(item)
        notifyDataSetChanged()
    }

    fun itemClear() {
        this.reserveList.clear()
        notifyDataSetChanged()
    }

    interface ReserveClick {
        fun reserve(reserve: PlateReservation)
    }

}
