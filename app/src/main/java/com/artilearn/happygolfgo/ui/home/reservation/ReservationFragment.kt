package com.artilearn.happygolfgo.ui.home.reservation

import android.content.Intent
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.TICKET
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.FragmentReservationBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.reservelist.ReserveListActivity
import com.artilearn.happygolfgo.ui.ticketdate.TicketDateActivity
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
                showFragmentToast(ToastType.ERROR, it)
            })
            networkFail.observe(this@ReservationFragment, Observer {
                showFragmentToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            reservationListButton.observe(this@ReservationFragment, Observer {
                reservationListCheck(adapter.itemCount)
            })
            swipeRefresh.observe(this@ReservationFragment, Observer {
                hideRefresh()
            })
            message.observe(this@ReservationFragment, Observer {
                showFragmentToast(ToastType.WARNNING, it)
            })
        }
    }

    private fun initAdapter() {
        adapter = ReservationAdapter { ticket ->
            with(ticket) {
                when {
                    !isUnlimited && remainingCount == 0 -> binding.parentLayout.snackbar(getString(R.string.home_fail_ticket))
                    allowToBook == false -> binding.parentLayout.snackbar(getString(R.string.home_reservation_allowbook_fail))
                    ticket.type == 1 -> "" // group lesson
                    else -> goSelectionDate(ticket)
                }
            }
        }

        binding.recyclerview.adapter = adapter
    }

    private fun initSwipeRefresh() {
        with(binding.swipeRefresh) {
            setOnRefreshListener {
                viewModel.ticketRefresh()
            }

            setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
            )
        }
    }

    private fun hideRefresh() {
        binding.swipeRefresh.isRefreshing = false
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

    private fun goSelectionDate(ticket: Ticket) {
        val selectIntent = Intent(activity, TicketDateActivity::class.java)
            .apply {
                putExtra(TICKET, ticket)
            }
        startActivity(selectIntent)
    }

}