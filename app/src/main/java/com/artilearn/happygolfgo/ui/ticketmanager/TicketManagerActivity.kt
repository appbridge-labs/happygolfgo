package com.artilearn.happygolfgo.ui.ticketmanager

import android.os.Bundle
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityTicketManagerBinding
import com.artilearn.happygolfgo.ui.ticketmanager.history.FragmentHistoryTicket
import com.artilearn.happygolfgo.ui.ticketmanager.over.FragmentOverTicket
import com.artilearn.happygolfgo.ui.ticketmanager.use.FragmentUseTicket
import org.koin.androidx.viewmodel.ext.android.viewModel

class TicketManagerActivity : BaseActivity<ActivityTicketManagerBinding>(
    R.layout.activity_ticket_manager
) {

    private val viewModel: TicketManagerViewModel by viewModel()
    private val useFragment by lazy(::FragmentUseTicket)
    private val overFragment by lazy(::FragmentOverTicket)
    private val historyFragment by lazy(::FragmentHistoryTicket)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserving()
    }

    private fun initBinding()  {
        binding.viewModel = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            tabPosition.observe(this@TicketManagerActivity, Observer { position ->
                initFragment(position)
            })
        }
    }

    private fun initFragment(position: Int) {
        when (position) {
            0 -> supportFragmentManager.beginTransaction().replace(R.id.frame_layout, useFragment).commit()
            1 -> supportFragmentManager.beginTransaction().replace(R.id.frame_layout, overFragment).commit()
            2 -> supportFragmentManager.beginTransaction().replace(R.id.frame_layout, historyFragment).commit()
            else -> throw IndexOutOfBoundsException("Not Found TicketManager Position: $position")
        }
    }

}