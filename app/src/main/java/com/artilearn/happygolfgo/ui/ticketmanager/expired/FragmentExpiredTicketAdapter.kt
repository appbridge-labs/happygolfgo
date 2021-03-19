package com.artilearn.happygolfgo.ui.ticketmanager.expired

import android.view.LayoutInflater
import android.view.ViewGroup
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseRecyclerViewAdapter
import com.artilearn.happygolfgo.databinding.ItemExpiredTicketBinding

typealias OnRecyclerViewItemClcik = (ExpiredTicketModel) -> Unit

class FragmentExpiredTicketAdapter(
    private val onItemClick: OnRecyclerViewItemClcik? = null
) : BaseRecyclerViewAdapter<ExpiredTicketModel>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ExpiredTicketModel> {
        return ItemViewHolder(
            ItemExpiredTicketBinding.inflate(
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
        private val binding: ItemExpiredTicketBinding
    ) : BaseViewHolder<ExpiredTicketModel>(binding) {
        override fun bind(item: ExpiredTicketModel) {
            super.bind(item)

            when (item.type) {
                0 -> binding.ivPoint.setImageResource(R.drawable.ic_reservation_lesson_point)
                else -> binding.ivPoint.setImageResource(R.drawable.ic_home_reservation_ticket_point)
            }
        }
    }
}