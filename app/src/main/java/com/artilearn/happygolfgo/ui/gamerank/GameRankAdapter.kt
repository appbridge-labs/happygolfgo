package com.artilearn.happygolfgo.ui.gamerank

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.databinding.ItemGroupRankBinding

class GameRankAdapter(private val items: ArrayList<GameRankTestModel>) : RecyclerView.Adapter<GameRankAdapter.GameRankHolder>() {

    lateinit var binding: ItemGroupRankBinding
    //private val items: MutableList<GameRankTestModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameRankHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_group_rank,
            parent,
            false
        )

        return GameRankHolder(binding).also {

        }
    }

    override fun onBindViewHolder(holder: GameRankHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class GameRankHolder(
        private val binding: ItemGroupRankBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GameRankTestModel) {
            //binding.item = item
            binding.tvRank.text = item.rank.toString()
            binding.tvNickname.text = item.nickname
            binding.tvScore.text = item.score.toString()
            binding.executePendingBindings()
        }
    }

    fun setItems(items: List<GameRankTestModel>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }
}