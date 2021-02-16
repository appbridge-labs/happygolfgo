package com.artilearn.happygolfgo.ui.ticketmanager.use

import android.widget.Toast
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentUsiesTicketBinding
import com.artilearn.happygolfgo.ui.ticketmanager.TicketManagerViewModel
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FragmentUseTicket : BaseFragment<FragmentUsiesTicketBinding, TicketManagerViewModel>(
    R.layout.fragment_usies_ticket
) {

    override val viewModel: TicketManagerViewModel by sharedViewModel()
    private lateinit var adapter: FragmentUseTicketAdapter

    override fun init() {
        initRecyclerView()
        dummyData()
    }

    private fun initRecyclerView() {
        adapter = FragmentUseTicketAdapter {
            Toast.makeText(requireActivity(), "$it", Toast.LENGTH_SHORT).show()
        }

        with(binding) {
            recyclerView.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            recyclerView.adapter = adapter
        }
    }

    private fun dummyData() {
        val list = mutableListOf<UseTicketModel>()
        for (i in 0..9) {
            println("?: $i")
            val item = UseTicketModel(
                "티켓 $i 번",
                "날짜 $i 번",
                true
            )
            item.isStoped = i % 2 == 0

            list.add(item)
        }

        adapter.submitList(list)
    }
}