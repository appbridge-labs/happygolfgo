package com.artilearn.happygolfgo.ui.gamescore

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseRecyclerViewAdapter
import com.artilearn.happygolfgo.base.Identifiable
import com.artilearn.happygolfgo.data.User
import com.artilearn.happygolfgo.databinding.ItemGolfGameScoreBinding
import com.artilearn.happygolfgo.databinding.ItemHomeAlramBinding


//class GameScoreAdapter() : ListAdapter<Any, GameScoreAdapter.GameScoreHolder>(
//    object : DiffUtil.ItemCallback<Any>() {
//        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
//            return oldItem == newItem
//        }
//
//        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
//            return false
//            //return oldItem == newItem
//        }
//    }
//) {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameScoreHolder {
//        return GameScoreHolder.from(parent).also { holder ->
//            /* .. */
//        }
//    }
//
//    override fun onBindViewHolder(holder: GameScoreHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//
//    class GameScoreHolder(
//        private val binding: ItemGolfGameScoreBinding
//    ) : RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(item: Any) {
//            binding.executePendingBindings()
//        }
//
//        companion object {
//            fun from(parent: ViewGroup): GameScoreHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ItemGolfGameScoreBinding.inflate(layoutInflater, parent, false)
//
//                return GameScoreHolder(binding)
//            }
//        }
//    }
//}