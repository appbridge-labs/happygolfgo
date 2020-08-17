package com.artilearn.happygolfgo.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.ItemHomeTicketBinding

class HomeAdapter(
    private val itemClick: (Ticket) -> Unit
) : RecyclerView.Adapter<HomeAdapter.TicketViewHolder>() {

    lateinit var binding: ItemHomeTicketBinding
    private val items: ArrayList<Ticket> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_home_ticket,
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
        private val binding: ItemHomeTicketBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: Ticket) {
            binding.ticket = ticket
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