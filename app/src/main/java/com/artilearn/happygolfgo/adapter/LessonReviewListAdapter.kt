package com.artilearn.happygolfgo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.databinding.ItemLessonListBinding
import com.artilearn.happygolfgo.models.LessonReviewList

class LessonReviewListAdapter(
    private val itemClick: (LessonReviewList) -> Unit
) : RecyclerView.Adapter<LessonReviewListAdapter.LRViewHolder>() {

    lateinit var binding: ItemLessonListBinding
    private val items: ArrayList<LessonReviewList> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LRViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_lesson_list,
            parent,
            false
        )

        return LRViewHolder(binding).also {
            binding.root.setOnClickListener { position ->
                itemClick(items[it.adapterPosition])
            }
        }

    }

    override fun onBindViewHolder(holder: LRViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class LRViewHolder(
        private val binding: ItemLessonListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(list: LessonReviewList) {
            binding.list = list
            binding.executePendingBindings()
        }
    }

    fun setItems(list: List<LessonReviewList>) {
        this.items.addAll(list)
        notifyDataSetChanged()
    }

    fun setClear() {
        this.items.clear()
        notifyDataSetChanged()
    }
}