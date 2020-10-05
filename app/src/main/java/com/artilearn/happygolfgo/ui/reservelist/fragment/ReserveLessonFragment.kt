package com.artilearn.happygolfgo.ui.reservelist.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.LESSON_ID
import com.artilearn.happygolfgo.constants.RESERVATION_LIST
import com.artilearn.happygolfgo.constants.RESERVATION_TYPE
import com.artilearn.happygolfgo.data.ReserveList
import com.artilearn.happygolfgo.databinding.FragmentReserveLessonBinding
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmActivity


class ReserveLessonFragment
    : BaseFragment<FragmentReserveLessonBinding, ReserveLessonViewModel>(R.layout.fragment_reserve_lesson) {

    private lateinit var adapter: ReserveListAdapter
    private val cancel = "cancel_lesson"

    companion object {

        fun newInstance(reservation: ArrayList<ReserveList>): ReserveLessonFragment {
            val args = Bundle()
            args.putParcelableArrayList(RESERVATION_LIST, reservation)
            val fragment = ReserveLessonFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val viewModel: ReserveLessonViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ReserveLessonViewModel() as T
            }
        }
    }

    override fun init() {
        initAdapter()

        val reservationLesson = arguments?.getParcelableArrayList<ReserveList>(RESERVATION_LIST)
        with(reservationLesson) {
            if (this?.size != 0) {
                emptyLessonView(false)
                setReservationLesson(this!!)
            } else {
                emptyLessonView(true)
            }
        }
    }

    private fun initAdapter() {
        adapter = ReserveListAdapter { lessonItem ->
            goReservationConfirm(lessonItem)
        }

        binding.recyclerview.adapter = adapter
    }

    private fun setReservationLesson(item: List<ReserveList>) {
        with(adapter) {
            clear()
            setItems(item)
        }
    }

    private fun emptyLessonView(status: Boolean) {
        binding.emptyLessonLayout.visibility = if (status) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun goReservationConfirm(item: ReserveList) {
        val confirmIntent = Intent(activity, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(LESSON_ID, item.id)
                putExtra(RESERVATION_TYPE, cancel)
            }
        startActivity(confirmIntent)
    }
}