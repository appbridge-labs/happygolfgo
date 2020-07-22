package com.artilearn.happygolfgo.ui.reserveconfirm

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.TICKET
import com.artilearn.happygolfgo.constants.TICKET_TIME
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.databinding.ActivityReserveConfirmBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.convertTime
import kotlinx.android.synthetic.main.activity_reserve_confirm.*
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class ReserveConfirmActivity : BaseActivity<ActivityReserveConfirmBinding>(R.layout.activity_reserve_confirm) {
    private val TAG = javaClass.simpleName

    private val ticket by lazy { intent.getParcelableExtra<Ticket>(TICKET) }
    private val ticketTime by lazy { intent.getParcelableExtra<TicketTime>(TICKET_TIME) }
    private lateinit var myApplication: MyApplication
    private val viewModel: ReserveConfirmViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ReserveConfirmViewModel(
                    myApplication.reserveConfirmRepository,
                    myApplication.reservationRepository
                ) as T
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
        initViewModelObserve()
        initView()
    }

    private fun inject() {
        myApplication = application as MyApplication
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            policy.observe(this@ReserveConfirmActivity, Observer {
                setPolicy(it)
            })
        }
    }

    private fun initView() {
        initToolbar(reserve_confirm_toolbar.toolBar, getString(R.string.reserve_confirm_toolbar_title), true)
        initBottomView(reserve_confirm_bottomView.bottomLayout, getString(R.string.reserve_confirm_bottom_title), BottimViewType.DEFAULT_TYPE)

        val datesList =  setTimeDate(ticketTime)

        with(binding) {
            tvTicketName.text = ticket.name
            tvTicketDate.text = datesList[0]
            tvTicketTime.text = datesList[1]
        }

        if (ticketTime.plateAvailabilityId != null) {
            viewModel.requestPolicy()
            viewModel.plateAvailabilityId = ticketTime.plateAvailabilityId
        } else {
            binding.tvTicketPolicy.text = getString(R.string.reserve_confirm_lesson_policy)
            viewModel.lessonAvailabilityId = ticketTime.lessonAvailabilityId
        }
        viewModel.ticketId = ticket.id
    }

    private fun setTimeDate(ticketTime: TicketTime): List<String> {
        val dates = mutableListOf<String>()

        try {
            val nowDate = convertTime(ticketTime.startDate).trim().split(":")
            val nowTime = convertTime(ticketTime.endDate).trim().split(":")

            dates.add("${nowDate[0]}. ${nowDate[1]}. ${nowDate[2]}")
            dates.add("${nowDate[3]}:${nowDate[4]} ~ ${nowTime[3]}:${nowTime[4]}")

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return dates
    }

    private fun setPolicy(policy: String) {
        binding.tvTicketPolicy.text = policy
    }

    override fun bottomAction() {
        super.bottomAction()
        if (ticketTime.plateAvailabilityId != null) {
            viewModel.reservationPlate()
        } else {
            viewModel.reservationLesson()
        }
    }
}