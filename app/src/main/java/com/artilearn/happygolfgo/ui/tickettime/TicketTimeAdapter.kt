package com.artilearn.happygolfgo.ui.tickettime

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.databinding.ItemTicketTimeBinding

class TicketTimeAdapter(
    private val itemClick: (TicketTime) -> Unit
) : RecyclerView.Adapter<TicketTimeAdapter.TicketTimeViewHolder>() {

    private val items: ArrayList<TicketTime> = arrayListOf()
    lateinit var binding: ItemTicketTimeBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketTimeViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_ticket_time,
            parent,
            false
        )

        return TicketTimeViewHolder(binding).also {
            binding.root.setOnClickListener { position ->
                itemClick(items[it.adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: TicketTimeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class TicketTimeViewHolder(
        private val binding: ItemTicketTimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TicketTime) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    fun setItems(ticketTime: List<TicketTime>) {
        this.items.addAll(ticketTime)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }
}