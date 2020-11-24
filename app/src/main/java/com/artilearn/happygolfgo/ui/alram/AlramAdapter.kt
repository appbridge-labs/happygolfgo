package com.artilearn.happygolfgo.ui.alram

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.Alram
import com.artilearn.happygolfgo.databinding.ItemAlramBinding

class AlramAdapter(
    private val itemClick: (Alram) -> Unit
) : RecyclerView.Adapter<AlramAdapter.AlramHolder>() {

    lateinit var binding: ItemAlramBinding
    private val items: ArrayList<Alram> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlramHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_alram,
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
        private val binding: ItemAlramBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(alram: Alram) {
            Log.d("testest", "bind: ${alram.eventType}")
            binding.alram = alram

            when (alram.eventType) {
                "3", "4", "6", "7" -> binding.ivArrow.visibility = View.VISIBLE
                else -> binding.ivArrow.visibility = View.GONE
            }

            when (alram.isRead) {
                false -> binding.rootLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.rootLayout.context,
                        R.color.color_alram_read
                    )
                )
                true -> binding.rootLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.rootLayout.context,
                        R.color.color_white
                    )
                )
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