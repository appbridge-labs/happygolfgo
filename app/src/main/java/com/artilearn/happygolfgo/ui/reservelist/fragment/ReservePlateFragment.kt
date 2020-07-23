package com.artilearn.happygolfgo.ui.reservelist.fragment

import android.content.Intent
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
import com.artilearn.happygolfgo.constants.*
import com.artilearn.happygolfgo.data.ReserveList
import com.artilearn.happygolfgo.databinding.FragmentReservePlateBinding
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmActivity
import kotlin.concurrent.fixedRateTimer

class ReservePlateFragment
    : BaseFragment<FragmentReservePlateBinding, ReservePlateViewModel>(R.layout.fragment_reserve_plate) {
    private val TAG = javaClass.simpleName

    private lateinit var adapter: ReserveListAdapter
    private val cancel = "cancel"

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

    }

    private fun initAdapter() {
        adapter = ReserveListAdapter { plateItem ->
            goReservationConfirm(plateItem)
        }

        binding.recyclerView.adapter = adapter
    }

    private fun setReservationPlate(item: List<ReserveList>) {
        with(adapter) {
            clear()
            setItems(item)
        }
    }

    private fun goReservationConfirm(item: ReserveList) {
        val confirmIntent = Intent(activity, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(RESERVATION, item)
                putExtra(CONFIRM_TYPE, cancel)
            }
        startActivity(confirmIntent)
    }
}