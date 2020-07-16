package com.artilearn.happygolfgo.ui.lessonlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.LessonLog
import com.artilearn.happygolfgo.databinding.ItemLessonListBinding

class LessonLogAdapter(
    private val itemClick: (LessonLog) -> Unit
) : RecyclerView.Adapter<LessonLogAdapter.LessonViewHolder>() {

    lateinit var binding: ItemLessonListBinding
    private val items: ArrayList<LessonLog> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_lesson_list,
            parent,
            false
        )

        return LessonViewHolder(binding).also {
            binding.root.setOnClickListener { position ->
                itemClick(items[it.adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class LessonViewHolder(
        private val binding: ItemLessonListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(log: LessonLog) {
            binding.log = log
            binding.executePendingBindings()
        }

    }

    fun setItems(logList: List<LessonLog>) {
        this.items.addAll(logList)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }
}