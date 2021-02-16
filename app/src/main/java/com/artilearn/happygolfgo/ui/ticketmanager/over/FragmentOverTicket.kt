package com.artilearn.happygolfgo.ui.ticketmanager.over

import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentOverTicketBinding
import com.artilearn.happygolfgo.ui.ticketmanager.TicketManagerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FragmentOverTicket : BaseFragment<FragmentOverTicketBinding, TicketManagerViewModel>(
    R.layout.fragment_over_ticket
) {
    override val viewModel: TicketManagerViewModel by sharedViewModel()

    override fun init() {

    }
}