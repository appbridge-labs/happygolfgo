package com.artilearn.happygolfgo.ui.home.reservation

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentReservationBinding
import com.artilearn.happygolfgo.ui.home.HomeActivity
import com.artilearn.happygolfgo.ui.home.HomeAdapter
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.snackbar
import com.tlsolution.tlsmodules.Logger.Companion.showError

class ReservationFragment
    : BaseFragment<FragmentReservationBinding, ReservationViewModel>(R.layout.fragment_reservation) {

    private lateinit var myApplication: MyApplication
    private lateinit var adapter: ReservationAdapter

    override val viewModel: ReservationViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ReservationViewModel(myApplication.homeRepository) as T
            }
        }
    }

    override fun init() {

        inject()
        initViewModelObserving()
        initAdapter()
        initSwipeRefresh()
    }

    private fun inject() {
        myApplication = activity?.application as MyApplication
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
}