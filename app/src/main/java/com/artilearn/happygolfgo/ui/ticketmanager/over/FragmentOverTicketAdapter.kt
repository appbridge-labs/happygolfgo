package com.artilearn.happygolfgo.ui.ticketmanager.over

import android.view.LayoutInflater
import android.view.ViewGroup
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseRecyclerViewAdapter
import com.artilearn.happygolfgo.databinding.ItemOverTicketBinding

typealias OnRecyclerViewItemClcik = (OverTicketModel) -> Unit

class FragmentOverTicketAdapter(
    private val onItemClick: OnRecyclerViewItemClcik? = null
) : BaseRecyclerViewAdapter<OverTicketModel>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<OverTicketModel> {
        return ItemViewHolder(
            ItemOverTicketBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).also {
            it.itemView.setOnClickListener { _ ->
                if (onItemClick == null) {
                    return@setOnClickListener
                } else {
                    val currentItem = currentList.getOrNull(it.adapterPosition) ?: return@setOnClickListener
                    onItemClick.invoke(currentItem)
                }
            }
        }
    }

    class ItemViewHolder(
        private val binding: ItemOverTicketBinding
    ) : BaseViewHolder<OverTicketModel>(binding) {
        override fun bind(item: OverTicketModel) {
            super.bind(item)

            when (item.type) {
                0 -> binding.ivPoint.setImageResource(R.drawable.ic_reservation_lesson_point)
                else -> binding.ivPoint.setImageResource(R.drawable.ic_home_reservation_ticket_point)
            }
        }
    }
}