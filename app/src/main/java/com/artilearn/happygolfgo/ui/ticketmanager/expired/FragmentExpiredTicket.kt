package com.artilearn.happygolfgo.ui.ticketmanager.expired

import android.widget.Toast
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentExpiredTicketBinding
import com.artilearn.happygolfgo.ui.ticketmanager.TicketManagerViewModel
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FragmentExpiredTicket : BaseFragment<FragmentExpiredTicketBinding, TicketManagerViewModel>(
    R.layout.fragment_expired_ticket
) {
    override val viewModel: TicketManagerViewModel by sharedViewModel()
    private lateinit var adapter: FragmentExpiredTicketAdapter

    override fun init() {
        initRecyclerView()
        dummyData()

        viewModel.getExpiredickets()
    }

    private fun initRecyclerView() {
        adapter = FragmentExpiredTicketAdapter { item ->
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
        val list = mutableListOf<ExpiredTicketModel>()
        for (i in 0..9) {
            val item: ExpiredTicketModel = if (i % 2 == 0) {
                ExpiredTicketModel(
                    "레슨 티켓 이름 $i 번",
                    "날짜 $i 번",
                    0
                )
            } else {
                ExpiredTicketModel(
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