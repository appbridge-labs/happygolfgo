package com.artilearn.happygolfgo.adapter.user

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.artilearn.happygolfgo.ui.fragment.LessonReservationListFragment
import com.artilearn.happygolfgo.ui.fragment.PlateReservationListFragment
import com.google.gson.internal.LinkedTreeMap


class MyReservationListAdapter(
    fragmentManager: FragmentManager,
    fragmentData: LinkedTreeMap<String, Any>
) : FragmentPagerAdapter(fragmentManager) {

    private lateinit var plateReservationListFragment: PlateReservationListFragment
    private lateinit var lessonReservationListFramgnet: LessonReservationListFragment

    init {
        plateReservationListFragment =
            PlateReservationListFragment(
                fragmentData
            )
        lessonReservationListFramgnet =
            LessonReservationListFragment(
                fragmentData
            )
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                plateReservationListFragment
            }
            else -> {
                lessonReservationListFramgnet
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "타석 예약"
            else -> "레슨 예약"
        }

    }
}