package com.artilearn.happygolfgo.ui.home.record

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyGolfPowerRankingAdapter (
    private val fa: FragmentActivity,
    private val pageIndex:Int
) : FragmentStateAdapter(fa) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 ->  GolfRankingInBranchFragment.newInstance(pageIndex)
            1 -> GolfRankingBySubjectFragment.newInstance(pageIndex)
            2 -> GolfRankingInAllBranchFragment.newInstance(pageIndex)
            else ->  GolfRankingInBranchFragment.newInstance(pageIndex)
        }
    }
}