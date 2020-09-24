package com.artilearn.happygolfgo.ui.home.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.ItemHomeReservationTicketBinding

class ReservationAdapter(
    private val itemClick: (Ticket) -> Unit
) : RecyclerView.Adapter<ReservationAdapter.TicketViewHolder>() {

    lateinit var binding: ItemHomeReservationTicketBinding
    private val items: ArrayList<Ticket> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_home_reservation_ticket,
            parent,
            false
        )

        return TicketViewHolder(binding).also {
            binding.root.setOnClickListener { position ->
                itemClick(items[it.adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class TicketViewHolder(
        private val binding: ItemHomeReservationTicketBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: Ticket) {
            binding.ticket = ticket
            when (ticket.type) {
                1 -> binding.ivArrow.visibility = View.INVISIBLE
                else -> binding.ivArrow.visibility = View.VISIBLE
            }
            binding.executePendingBindings()
        }

    }

    fun setItems(tickets: List<Ticket>) {
        this.items.addAll(tickets)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }
}