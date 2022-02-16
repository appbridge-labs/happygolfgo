package com.artilearn.happygolfgo.ui.home.reservation

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.TICKET
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.FragmentReservationBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.home.HomeViewModel
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.EXPIRED_TICKET
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.PAUSE_TICKET
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.USE_TICKET
import com.artilearn.happygolfgo.ui.reservelist.ReserveListActivity
import com.artilearn.happygolfgo.ui.ticketdate.TicketDateActivity
import com.artilearn.happygolfgo.util.*
import jp.wasabeef.recyclerview.animators.LandingAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ReservationFragment
    : BaseFragment<FragmentReservationBinding, ReservationViewModel>(R.layout.fragment_reservation) {

    override val viewModel: ReservationViewModel by viewModel()
    //private lateinit var adapter: ReservationAdapter
    private lateinit var adapter: ReservationNewAdapter

    override fun init() {

        initBinding()
        initViewModelObserving()
        initAdapter()
    }

    override fun onStart() {
        super.onStart()
        /**
         * 뷰모델에 있었지만 현재 급한 관계로 여기서 통신함...(전체)
         * 각 화면에서 유저 액션이 있을 경우 리시버로 해당 아이템모델을 교체해줄 필요
         * 혹은 캐싱데이터로 가지고 놀기, 여유있을 때 수정요망
         */
        viewModel.getTicket()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            serverError.observe(viewLifecycleOwner, Observer {
                showFragmentToast(ToastType.ERROR, it)
            })
            networkFail.observe(viewLifecycleOwner, Observer {
                showFragmentToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            reservationListButton.observe(viewLifecycleOwner, Observer {
                reservationListCheck(adapter.itemCount)
            })
            message.observe(viewLifecycleOwner, Observer {
                showFragmentToast(ToastType.WARNNING, it)
            })
            appRefresh.observe(viewLifecycleOwner, Observer {
                goRefreshFromFragment()
            })
        }
    }

    private fun initAdapter() {
        adapter = ReservationNewAdapter { ticket ->
            when (ticket.viewType) {
                USE_TICKET -> {
                    with(ticket) {
                        val startDateEquals = convertDate(startDate)?.compareTo(getNowDate()) == -1

                        when {
                            !isUnlimited && remainingCount == 0 -> {
                                binding.parentLayout.snackbar(getString(R.string.home_fail_ticket))
                            }
                            allowToBook == false -> {
                                when (ignoreLimit) {
                                    true -> {
                                        showContinueMessage(ticket)
                                    }
                                    else -> binding.parentLayout.snackbar("예약은 최대 ${ticket.maxReservationCount}개 까지만 가능합니다")
                                }
                            }
                            ticket.type == 1 -> {
                                // group lesson
                            }
                            else -> {
                                goSelectionDate(ticket)
                            }
                        }
                    }
                }
                PAUSE_TICKET -> {
                    binding.parentLayout.snackbar("정지중인 이용권입니다")
                }
                EXPIRED_TICKET -> {
                    binding.parentLayout.snackbar("만료된 이용권입니다")
                }
            }
        }//end of adapter

        with(binding) {
            recyclerview.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            recyclerview.adapter = adapter
        }
    }

    private fun showContinueMessage(ticket: Ticket) {
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setTitle(getString(R.string.fragment_alert_title_ignore_max_reservation))
            .setMessage(R.string.fragment_alert_message_ignore_max_reservation)
            .setPositiveButton(getString(R.string.fragment_alert_okay_ignore_max_reservation)) { _, _ ->
                goSelectionDate(ticket)
            }
            .setNegativeButton(getString(R.string.fragment_alert_cancel_ignore_max_reservation)) { _, _ ->  }
        dialog.create()
        dialog.show()
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
        val selectIntent = Intent(activity, TicketDateActivity::class.java).apply {
                putExtra(TICKET, ticket)
            }
        startActivity(selectIntent)
    }

}