package com.artilearn.happygolfgo.ui.home.alram

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.Alram
import com.artilearn.happygolfgo.databinding.ItemHomeAlramBinding

class AlramAdapter(
    private val itemClick: (Alram) -> Unit
) : RecyclerView.Adapter<AlramAdapter.AlramHolder>() {

    lateinit var binding: ItemHomeAlramBinding
    private val items: ArrayList<Alram> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlramHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_home_alram,
            parent,
            false
        )

        return AlramHolder(binding).also {
            binding.root.setOnClickListener { position ->
                itemClick(items[it.adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: AlramHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class AlramHolder(
        private val binding: ItemHomeAlramBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Alram) {
            binding.item = item

            when (item.eventType) {
                "3", "4", "6", "7" -> binding.ivArrow.visibility = View.VISIBLE
                else -> binding.ivArrow.visibility = View.GONE
            }

            when (item.isRead) {
                false -> binding.parentLayout.setBackgroundResource(R.drawable.bg_alram_unread)
                true -> binding.parentLayout.setBackgroundResource(R.drawable.bg_alram_read)
            }

            binding.executePendingBindings()
        }
    }

    fun setItems(alram: List<Alram>) {
        this.items.addAll(alram)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }
}