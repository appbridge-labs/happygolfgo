package com.artilearn.happygolfgo.ui.reservelist.fragment

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.RESERVATION_LIST
import com.artilearn.happygolfgo.data.ReserveList
import com.artilearn.happygolfgo.data.reservelist.ReserveListResponse
import com.artilearn.happygolfgo.databinding.FragmentReserveLessonBinding


class ReserveLessonFragment
    : BaseFragment<FragmentReserveLessonBinding, ReserveLessonViewModel>(R.layout.fragment_reserve_lesson) {
    private val TAG = javaClass.simpleName

    private lateinit var adapter: ReserveListAdapter

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
        reservationLesson?.let {
            setReservationLesson(it)
        }
    }

    private fun initAdapter() {
        adapter = ReserveListAdapter { reservationItem ->
            Toast.makeText(activity, "$reservationItem", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerView.adapter = adapter
    }

    private fun setReservationLesson(item: List<ReserveList>) {
        with(adapter) {
            clear()
            setItems(item)
        }
    }
}