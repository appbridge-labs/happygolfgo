package com.artilearn.happygolfgo.ui.ticketmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
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
            0 -> setFragment(useFragment)
            1 -> setFragment(overFragment)
            2 -> setFragment(historyFragment)
            else -> throw IndexOutOfBoundsException("Not Found TicketManager Position: $position")
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            setCustomAnimations(R.anim.from_up_in, R.anim.from_up_out)
            replace(R.id.frame_layout, fragment).commit()
        }
    }

}