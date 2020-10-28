package com.artilearn.happygolfgo.ui.home.reservation

import android.content.Intent
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentReservationBinding
import com.artilearn.happygolfgo.ui.reservelist.ReserveListActivity
import com.artilearn.happygolfgo.util.snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReservationFragment
    : BaseFragment<FragmentReservationBinding, ReservationViewModel>(R.layout.fragment_reservation) {

    override val viewModel: ReservationViewModel by viewModel()
    private lateinit var adapter: ReservationAdapter

    override fun init() {

        initBinding()
        initViewModelObserving()
        initAdapter()
        initSwipeRefresh()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            serverError.observe(this@ReservationFragment, Observer {
                showFragmentError(it)
            })
            networkFail.observe(this@ReservationFragment, Observer {
                showFragmentNetworkFail()
            })
            reservationListButton.observe(this@ReservationFragment, Observer {
                reservationListCheck(adapter.itemCount)
            })
        }
    }

    private fun initAdapter() {
        adapter = ReservationAdapter { ticket ->
            with(ticket) {
                when {
                    !isUnlimited && remainingCount == 0 -> binding.parentLayout.snackbar(getString(R.string.home_fail_ticket))
                    allowToBook == false -> binding.parentLayout.snackbar(getString(R.string.home_reservation_allowbook_fail))
                    ticket.type == 1 -> ""
                    else -> ""//goSelectionDate(ticket)
                }
            }
        }

        binding.recyclerview.adapter = adapter
    }

    private fun initSwipeRefresh() {
        with(binding.swipeRefresh) {
            setOnRefreshListener {
                viewModel.getTicket()
                isRefreshing = false
            }

            setColorSchemeColors(resources.getColor(R.color.color_dark_green))
        }
    }

    private fun reservationListCheck(item: Int) {
        if (item != 0) {
            goReservationList()
        } else {
            binding.parentLayout.snackbar(getString(R.string.home_empty_ticket_go))
        }
    }

    private fun goReservationList() {
        val intent = Intent(activity, ReserveListActivity::class.java)
        startActivity(intent)
    }

}