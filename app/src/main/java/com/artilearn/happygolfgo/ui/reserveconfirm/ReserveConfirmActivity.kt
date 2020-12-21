package com.artilearn.happygolfgo.ui.reserveconfirm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.*
import com.artilearn.happygolfgo.data.ReservationLesson
import com.artilearn.happygolfgo.data.ReservationPlate
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.databinding.ActivityReserveConfirmBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.reservesuccess.ReserveSuccessActivity
import com.artilearn.happygolfgo.ui.tickettime.TicketTimeActivity
import com.artilearn.happygolfgo.util.convertCalendar
import com.artilearn.happygolfgo.util.convertDate
import com.artilearn.happygolfgo.util.convertTime
import kotlinx.android.synthetic.main.activity_reserve_confirm.*
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ReserveConfirmActivity :
    BaseActivity<ActivityReserveConfirmBinding>(R.layout.activity_reserve_confirm) {

    private val viewModel: ReserveConfirmViewModel by viewModel()
    private val lessonId by lazy { intent.getIntExtra(LESSON_ID, 0) }
    private val plateId by lazy { intent.getIntExtra(PLATE_ID, 0) }
    private val reservationType by lazy { intent.getStringExtra(RESERVATION_TYPE) }
    private val ticketTime by lazy { intent.getParcelableExtra<TicketTime>(TICKET_TIME) }
    private val ticket by lazy { intent.getParcelableExtra<Ticket>(TICKET) }
    private val timeChange by lazy { intent.getIntExtra(PLATE_TIME_CHANGE, 0) }

    private lateinit var secondTicket: Ticket
    private var secondPlateId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initView()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            networkFail.observe(this@ReserveConfirmActivity, Observer {
                showNetworkFail()
            })
            serverError.observe(this@ReserveConfirmActivity, Observer { errorMsg ->
                showToast(ToastType.ERROR, errorMsg)
            })
            status.observe(this@ReserveConfirmActivity, Observer {
                when (status.value) {
                    ReserveConfirmViewModel.ReservationStatus.CANCEL_SUCCESS -> goReservationCancelSuccess()
                    ReserveConfirmViewModel.ReservationStatus.CANCEL_FAIL -> showToast(
                        ToastType.ERROR,
                        getString(R.string.reserve_confirm_cancel_fail)
                    )
                }
            })
            plateModel.observe(this@ReserveConfirmActivity, Observer {
                if (ticket == null) {
                    secondPlateId = it.plateReservation.id
                    with(it.plateReservation.ticket) {
                        secondTicket = Ticket(
                            id = id,
                            name = name,
                            type = type,
                            period = period,
                            totalCount = totalCount,
                            usedCount = usedCount,
                            onlyOnceToday = onlyOnceToday,
                            remainingCount = remainingCount,
                            isUnlimited = isUnlimited,
                            startDate = it.plateReservation.startDate,
                            endDate = it.plateReservation.endDate,
                            allowToBook = null
                        )
                    }
                }
                if (it.plateReservation.isCancellable) {
                    showChangeTimeLayout()
                    initBottomView(binding.reserveConfirmBottomView.bottom_layout, getString(R.string.reserve_confirm_cancel_bottom_title), BottimViewType.CANCEL_TYPE)
                }
                setReservationTicketName(it.plateReservation.name)
                setReservationTicketDate(it.plateReservation.startDate)
                setReservationTicketTime(it.plateReservation.startDate, it.plateReservation.endDate)
                setReservationPolicy(it.policy.body)
            })
            lessonModel.observe(this@ReserveConfirmActivity, Observer {
                if (it.lessonReservation.isCancellable) {
                    initBottomView(
                        binding.reserveConfirmBottomView.bottom_layout,
                        getString(R.string.reserve_confirm_cancel_bottom_title),
                        BottimViewType.CANCEL_TYPE
                    )
                }
                setReservationTicketName(it.lessonReservation.name)
                setReservationTicketDate(it.lessonReservation.startDate)
                setReservationTicketTime(
                    it.lessonReservation.startDate,
                    it.lessonReservation.endDate
                )
            })
            policy.observe(this@ReserveConfirmActivity, Observer {
                setReservationPolicy(it)
            })
            unKnowError.observe(this@ReserveConfirmActivity, Observer {
                showToast(ToastType.WARNNING, "티켓 정보를 알 수 없습니다")
            })
            reservationPlate.observe(this@ReserveConfirmActivity, Observer {
                goReserveSuccessPlate(it)
            })
            reservationLesson.observe(this@ReserveConfirmActivity, Observer {
                goReserveSuccessLesson(it)
            })
            buttonClickable.observe(this@ReserveConfirmActivity, Observer {
                binding.reserveConfirmBottomView.bottom_layout.isClickable = true
            })
            reservationTimeChange.observe(this@ReserveConfirmActivity, Observer {
                goReserveSuccessPlateTime(it)
            })
            appRefresh.observe(this@ReserveConfirmActivity, Observer {
                goRefresh()
            })
        }
    }

    private fun initView() {
        hideChangeTimeLayout()
        initToolbar(reserve_confirm_toolbar.toolbar, getString(R.string.reserve_confirm_toolbar_title), true)

        when (reservationType) {
            "ReservationPlateFromAlram" -> viewModel.getReservationPlateDetail(plateId)
            "ReservationLessonFromAlram" -> {
                viewModel.getReservationLessonDetail(lessonId)
                binding.tvTicketPolicy.text = getString(R.string.reserve_confirm_lesson_policy)
            }
            "cancel_plate" -> viewModel.getReservationPlateDetail(plateId)
            "cancel_lesson" -> {
                viewModel.getReservationLessonDetail(lessonId)
                binding.tvTicketPolicy.text = getString(R.string.reserve_confirm_lesson_policy)
            }
            "reservation" -> {
                initBottomView(binding.reserveConfirmBottomView.bottom_layout, getString(R.string.reserve_confirm_bottom_title), BottimViewType.DEFAULT_TYPE)
                if (ticketTime.plateAvailabilityId != null) {
                    viewModel.ticketId = ticket.id
                    viewModel.plateAvailabilityId = ticketTime.plateAvailabilityId
                    viewModel.plateTimeChange = timeChange
                    viewModel.requestPolicy()
                } else {
                    viewModel.ticketId = ticket.id
                    viewModel.lessonAvailabilityId = ticketTime.lessonAvailabilityId
                    binding.tvTicketPolicy.text = getString(R.string.reserve_confirm_lesson_policy)
                }
                setReservationTicketName(ticket.name)
                setReservationTicketDate(ticketTime.startDate)
                setReservationTicketTime(ticketTime.startDate, ticketTime.endDate)
            }
        }
    }

    private fun setReservationTicketName(name: String) {
        binding.tvTicketName.text = name
    }

    private fun setReservationTicketDate(startTime: String) {
        val date = convertTime(startTime).trim().split(":")
        val text = "${date[0]}. ${date[1]}. ${date[2]}"
        binding.tvTicketDate.text = text
    }

    private fun setReservationTicketTime(startTime: String, endTime: String) {
        val start = convertTime(startTime).trim().split(":")
        val end = convertTime(endTime).trim().split(":")

        val text = "${start[3]}:${start[4]} ~ ${end[3]}:${end[4]}"
        binding.tvTicketTime.text = text
    }

    private fun setReservationPolicy(policy: String) {
        binding.tvTicketPolicy.text = policy
    }

    private fun showChangeTimeLayout() {
        with(binding) {
            llTimechange.visibility = View.VISIBLE
            llTimechange.setOnClickListener { goTicketTime() }
        }
    }

    private fun hideChangeTimeLayout() {
        binding.llTimechange.visibility = View.GONE
    }

    private fun goReserveSuccessPlate(plate: ReservationPlate) {
        val plateIntent = Intent(this, ReserveSuccessActivity::class.java)
            .apply {
                putExtra(RESERVATION_PLATE, plate)
                putExtra(RESERVATION_TYPE, reservationType)
            }
        startActivity(plateIntent)
    }

    private fun goReserveSuccessLesson(lesson: ReservationLesson) {
        val lessonIntent = Intent(this, ReserveSuccessActivity::class.java)
            .apply {
                putExtra(RESERVATION_LESSON, lesson)
                putExtra(RESERVATION_TYPE, reservationType)
            }
        startActivity(lessonIntent)
    }

    private fun goReservationCancelSuccess() {
        val cancelIntent = Intent(this, ReserveSuccessActivity::class.java)
            .apply {
                putExtra(RESERVATION_TYPE, reservationType)
            }
        startActivity(cancelIntent)
        finish()
    }

    private fun goTicketTime() {
        val intent = Intent(this, TicketTimeActivity::class.java)

        if (ticket == null) {
            val convertTime = convertDate(secondTicket.startDate)
            val calendar = convertTime?.let { convertCalendar(it) }
            val date = "${calendar?.get(Calendar.YEAR)}-${calendar?.get(Calendar.MONTH)?.plus(1)}-${calendar?.get(
                Calendar.DAY_OF_MONTH)}"
            secondTicket.startDate = date

            with(intent) {
                putExtra(TICKET, secondTicket)
                putExtra(SELECTION_DATE, date)
                putExtra(PLATE_TIME_CHANGE, secondPlateId)
            }
        } else {
            with(intent) {
                putExtra(TICKET, ticket)
                putExtra(SELECTION_DATE, ticket.startDate)
                putExtra(PLATE_TIME_CHANGE, plateId)
            }
        }

        startActivity(intent)
    }


    private fun goReserveSuccessPlateTime(plate: ReservationPlate) {
        val plateIntent = Intent(this, ReserveSuccessActivity::class.java)
            .apply {
                putExtra(RESERVATION_PLATE, plate)
                putExtra(RESERVATION_TYPE, reservationType)
                putExtra(PLATE_TIME_CHANGE, 1)
            }
        startActivity(plateIntent)
    }

    override fun bottomAction() {
        super.bottomAction()

        when (reservationType) {
            "ReservationPlateFromAlram", "cancel_plate" -> viewModel.reservationPlateCancel(plateId)
            "ReservationLessonFromAlram", "cancel_lesson" -> viewModel.reservationLessonCancel(lessonId)
            "reservation" -> {
                if (ticketTime.plateAvailabilityId != null) {
                    if (timeChange == 0) viewModel.reservationPlate()
                    else viewModel.reservationPlateTimeChange()
                } else {
                    viewModel.reservationLesson()
                }
            }
        }

        binding.reserveConfirmBottomView.bottom_layout.isClickable = false
    }

}