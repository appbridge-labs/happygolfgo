package com.artilearn.happygolfgo.ui.ticketmanager.over

import android.widget.Toast
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentOverTicketBinding
import com.artilearn.happygolfgo.ui.ticketmanager.TicketManagerViewModel
import com.artilearn.happygolfgo.ui.ticketmanager.use.UseTicketModel
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FragmentOverTicket : BaseFragment<FragmentOverTicketBinding, TicketManagerViewModel>(
    R.layout.fragment_over_ticket
) {
    override val viewModel: TicketManagerViewModel by sharedViewModel()
    private lateinit var adapter: FragmentOverTicketAdapter

    override fun init() {
        initRecyclerView()
        dummyData()
    }

    private fun initRecyclerView() {
        adapter = FragmentOverTicketAdapter { item ->
            Toast.makeText(this.context, "클릭하면 다이얼로그로 티켓에 대한 상세 정보들을 보여줄 것 $item", Toast.LENGTH_SHORT).show()
        }

        with(binding) {
            recyclerView.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            recyclerView.adapter = adapter
        }
    }

    private fun dummyData() {
        val list = mutableListOf<OverTicketModel>()
        for (i in 0..9) {
            val item: OverTicketModel = if (i % 2 == 0) {
                OverTicketModel(
                    "레슨 티켓 이름 $i 번",
                    "날짜 $i 번",
                    0
                )
            } else {
                OverTicketModel(
                    "타석 티켓 이름 $i 번",
                    "날짜 $i 번",
                    1
                )
            }

            list.add(item)
        }

        adapter.submitList(list)
    }
}