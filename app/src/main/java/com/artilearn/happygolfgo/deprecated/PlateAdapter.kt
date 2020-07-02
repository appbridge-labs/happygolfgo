package com.artilearn.happygolfgo.deprecated

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.databinding.ItemPlateSelectionBinding
import com.artilearn.happygolfgo.models.Plate


//class PlateAdapter(private val listener: PlateClickListener) :
//    ListAdapter<Plate, PlateAdapter.PlateViewHolder>(
//        DiffCallback()
//    ) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlateViewHolder {
//        return PlateViewHolder.from(
//            parent
//        )
//    }
//
//    override fun onBindViewHolder(holder: PlateViewHolder, position: Int) {
//        holder.bind(listener, getItem(position)!!)
//    }
//
//    class PlateViewHolder(private val binding: ItemPlateSelectionBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(listener: PlateClickListener, item: Plate) {
//            binding.plate = item
//            binding.listener = listener
//            binding.executePendingBindings()
//        }
//
//        companion object {
//            fun from(parent: ViewGroup): PlateViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ItemPlateSelectionBinding.inflate(layoutInflater, parent, false)
//
//                return PlateViewHolder(
//                    binding
//                )
//            }
//        }
//    }
//
//    class DiffCallback : DiffUtil.ItemCallback<Plate>() {
//        override fun areItemsTheSame(oldItem: Plate, newItem: Plate): Boolean {
//            return oldItem == newItem
//        }
//
//        override fun areContentsTheSame(oldItem: Plate, newItem: Plate): Boolean {
//            return oldItem.id == newItem.id
//        }
//    }
//
//    class PlateClickListener(val listener: (plate: Plate) -> Unit) {
//        fun onClicked(plate: Plate) = listener(plate)
//    }
//}

