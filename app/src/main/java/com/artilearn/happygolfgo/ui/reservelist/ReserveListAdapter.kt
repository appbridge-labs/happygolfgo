package com.artilearn.happygolfgo.ui.reservelist

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.artilearn.happygolfgo.data.ReserveList
import com.artilearn.happygolfgo.databinding.ItemReserveListBinding

//class ReserveListAdapter(
//    private val fa: FragmentActivity,
//    private val fragments: ArrayList<Fragment>
//) : FragmentStateAdapter(fa) {
//
//    override fun getItemCount() = fragments.size
//
//    override fun createFragment(position: Int): Fragment = fragments[position]
//}

class ReserveListAdapter(
    private val fragments: ArrayList<Fragment>
) : RecyclerView.Adapter<ReserveListAdapter.FragmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount() = fragments.size

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class FragmentViewHolder(
        private val binding: ItemReserveListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReserveList) {

        }
    }
}