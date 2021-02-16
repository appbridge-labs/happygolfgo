package com.artilearn.happygolfgo.ui.ticketmanager.history

import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentHistoryTicketBinding
import com.artilearn.happygolfgo.ui.ticketmanager.TicketManagerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FragmentHistoryTicket : BaseFragment<FragmentHistoryTicketBinding, TicketManagerViewModel>(
    R.layout.fragment_history_ticket
) {
    override val viewModel: TicketManagerViewModel by sharedViewModel()

    override fun init() {

    }
}