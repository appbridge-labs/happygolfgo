package com.artilearn.happygolfgo.ui.reservelist.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.RESERVATION_LIST
import com.artilearn.happygolfgo.data.ReserveList
import com.artilearn.happygolfgo.databinding.FragmentReservePlateBinding
import kotlin.concurrent.fixedRateTimer

class ReservePlateFragment
    : BaseFragment<FragmentReservePlateBinding, ReservePlateViewModel>(R.layout.fragment_reserve_plate) {
    private val TAG = javaClass.simpleName

    private lateinit var adapter: ReserveListAdapter

    companion object {

        fun newInstance(reservation: ArrayList<ReserveList>): ReservePlateFragment {
            val args = Bundle()
            args.putParcelableArrayList(RESERVATION_LIST, reservation)
            val fragment = ReservePlateFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val viewModel: ReservePlateViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ReservePlateViewModel() as T
            }
        }
    }

    override fun init() {
        initAdapter()

        val reservationPlate = arguments?.getParcelableArrayList<ReserveList>(RESERVATION_LIST)

        reservationPlate?.let {
            setReservationPlate(it)
        }

        binding.recyclerView.adapter = adapter
    }

    private fun initAdapter() {
        adapter = ReserveListAdapter { reservationItem ->
            Toast.makeText(activity, "$reservationItem", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setReservationPlate(item: List<ReserveList>) {
        with(adapter) {
            clear()
            setItems(item)
        }
    }
}