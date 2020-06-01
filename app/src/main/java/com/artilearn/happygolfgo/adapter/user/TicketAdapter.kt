package com.artilearn.happygolfgo.adapter.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.models.Ticket
import com.artilearn.happygolfgo.util.convertTime

class TicketAdapter : RecyclerView.Adapter<TicketAdapter.ItemViewHolder>() {

    var listener: TicketClick? = null
    private val ticketList: ArrayList<Ticket> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_ticket_card_view, parent, false
        )

        return ItemViewHolder(view).also {
            it.itemView.setOnClickListener { position ->
                val currentItem =
                    ticketList.getOrNull(it.adapterPosition) ?: return@setOnClickListener
                listener?.ticket(currentItem)
            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(ticketList[position])
    }

    override fun getItemCount() = ticketList.size

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.text_view_title_ticket)
        val day = itemView.findViewById<TextView>(R.id.text_view_period_ticket)
        val count = itemView.findViewById<TextView>(R.id.text_view_remaining_count_ticket)

        fun bind(item: Ticket) {
            val start = convertTime(item.startDate).trim().split(":")
            val end = convertTime(item.endDate).trim().split(":")

            title.text = item.name
            day.text = "${start[1]}.${start[2]}~${end[1]}.${end[2]}"

            if (item.isUnlimited && item.onlyOnceToday) {
                count.text = "1일 1회 무제한 사용"
            } else if (item.isUnlimited && !item.onlyOnceToday) {
                count.text = "무제한 사용"
            } else {
                count.text =
                    item.totalCount.toString() + "회 중 " + item.remainingCount.toString() + "회 남음"
            }
        }
    }

    fun setItem(item: List<Ticket>) {
        this.ticketList.addAll(item)
        notifyDataSetChanged()
    }

    fun itemClear() {
        this.ticketList.clear()
    }

    interface TicketClick {
        fun ticket(ticket: Ticket)
    }
}
