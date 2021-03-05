package com.artilearn.happygolfgo.ui.ticketmanager.use

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artilearn.happygolfgo.base.BaseRecyclerViewAdapter
import com.artilearn.happygolfgo.databinding.ItemUseTicketBinding

typealias OnRecyclerViewItemClick = (UseTicketModel) -> Unit

class FragmentUseTicketAdapter(
    private val onItemClick: OnRecyclerViewItemClick? = null
) : BaseRecyclerViewAdapter<UseTicketModel>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<UseTicketModel> {
        return ItemViewHolder(
            ItemUseTicketBinding.inflate(
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
        private val binding: ItemUseTicketBinding
    ) : BaseViewHolder<UseTicketModel>(binding) {
        override fun bind(item: UseTicketModel) {
            super.bind(item)

            with(binding) {
                if (item.isStoped) {
                    tvTicketStop.text = "일시 정지 중"
                    tvTicketStop.visibility = View.VISIBLE
                } else {
                    tvTicketStop.text = null
                    tvTicketStop.visibility = View.GONE
                }
            }
        }
    }

}