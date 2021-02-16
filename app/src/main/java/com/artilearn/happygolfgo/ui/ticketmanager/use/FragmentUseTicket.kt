package com.artilearn.happygolfgo.ui.ticketmanager.use

import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentUsiesTicketBinding
import com.artilearn.happygolfgo.ui.ticketmanager.TicketManagerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FragmentUseTicket : BaseFragment<FragmentUsiesTicketBinding, TicketManagerViewModel>(
    R.layout.fragment_usies_ticket
) {

    override val viewModel: TicketManagerViewModel by sharedViewModel()

    override fun init() {

    }
}