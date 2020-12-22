package com.artilearn.happygolfgo.ui.reservelist.fragment

import android.content.Intent
import android.os.Bundle
<<<<<<< HEAD
import android.view.View
=======
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
>>>>>>> hot-test-3
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.PLATE_ID
import com.artilearn.happygolfgo.constants.RESERVATION_LIST
import com.artilearn.happygolfgo.constants.RESERVATION_TYPE
import com.artilearn.happygolfgo.data.ReserveList
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.FragmentReservePlateBinding
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmActivity
<<<<<<< HEAD
import org.koin.androidx.viewmodel.ext.android.viewModel
=======
import com.artilearn.happygolfgo.util.convertCalendar
import com.artilearn.happygolfgo.util.convertDate
import java.util.*
>>>>>>> hot-test-3

class ReservePlateFragment
    : BaseFragment<FragmentReservePlateBinding, ReservePlateViewModel>(R.layout.fragment_reserve_plate) {

    override val viewModel: ReservePlateViewModel by viewModel()
    private lateinit var adapter: ReserveListAdapter
    private val cancel = "cancel_plate"

    companion object {

        fun newInstance(reservation: ArrayList<ReserveList>): ReservePlateFragment {
            val args = Bundle()
            args.putParcelableArrayList(RESERVATION_LIST, reservation)
            val fragment = ReservePlateFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun init() {
        initAdapter()

        val reservationPlate = arguments?.getParcelableArrayList<ReserveList>(RESERVATION_LIST)
        with(reservationPlate) {
            if (this?.size != 0) {
                emptyPlateView(false)
                setReservationPlate(this!!)
            } else {
                emptyPlateView(true)
            }
        }
    }

    private fun initAdapter() {
        adapter = ReserveListAdapter { plateItem ->
            goReservationConfirm(plateItem)
        }

        binding.recyclerview.adapter = adapter
    }

    private fun setReservationPlate(item: List<ReserveList>) {
        with(adapter) {
            clear()
            setItems(item)
        }
    }

    private fun emptyPlateView(stauts: Boolean) {
        binding.emptyPlateLayout.visibility = if (stauts) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun goReservationConfirm(item: ReserveList) {
        val ticket = Ticket(
            id = item.ticket?.id ?: 0,
            type = item.ticket?.type ?: 0,
            name = item.name,
            period = item.ticket?.period ?: 0,
            totalCount = item.totalCount,
            usedCount = item.ticket?.usedCount ?: 0,
            onlyOnceToday = item.ticket?.onlyOnceToday ?: false,
            remainingCount = item.remainingCount,
            isUnlimited = item.ticket?.isUnlimited ?: false,
            startDate = item.startDate,
            endDate = item.endDate,
            allowToBook = false
        )

        val convertTime = convertDate(ticket.startDate)
        val calendar = convertTime?.let { convertCalendar(it) }
        val date = "${calendar?.get(Calendar.YEAR)}-${calendar?.get(Calendar.MONTH)?.plus(1)}-${calendar?.get(Calendar.DAY_OF_MONTH)}"
        ticket.startDate = date

        val confirmIntent = Intent(activity, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(PLATE_ID, item.id)
                putExtra(RESERVATION_TYPE, cancel)
                putExtra(TICKET, ticket)
            }

        startActivity(confirmIntent)
    }
}