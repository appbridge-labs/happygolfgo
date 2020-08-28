package com.artilearn.happygolfgo.ui.reserveconfirm

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.convertTime
import kotlinx.android.synthetic.main.activity_reserve_confirm.*
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class ReserveConfirmActivity :
    BaseActivity<ActivityReserveConfirmBinding>(R.layout.activity_reserve_confirm) {
    private val TAG = javaClass.simpleName

    private lateinit var myApplication: MyApplication

    private val lessonId by lazy {
        intent.getIntExtra(LESSON_ID, 0)
    }

    private val plateId by lazy {
        intent.getIntExtra(PLATE_ID, 0)
    }

    private val reservationType by lazy {
        intent.getStringExtra(RESERVATION_TYPE)
    }

    private val ticketTime by lazy {
        intent.getParcelableExtra<TicketTime>(TICKET_TIME)
    }

    private val ticket by lazy {
        intent.getParcelableExtra<Ticket>(TICKET)
    }

    private val viewModel: ReserveConfirmViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ReserveConfirmViewModel(
                    myApplication.reserveConfirmRepository,
                    myApplication.reservationRepository,
                    myApplication.cancelRepository
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
//            policy.observe(this@ReserveConfirmActivity, Observer {
//                setPolicy(it)
//            })
//            plateModel.observe(this@ReserveConfirmActivity, Observer { plateResponse ->
//                goReserveSuccessPlate(plateResponse)
//            })
//            lessonModel.observe(this@ReserveConfirmActivity, Observer { lessonResponse ->
//                goReserveSuccessLesson(lessonResponse)
//            })
//            unKnowError.observe(this@ReserveConfirmActivity, Observer {
//                showToast(ToastType.ERROR, getString(R.string.default_error_toast))
//            })
            networkFail.observe(this@ReserveConfirmActivity, Observer {
                showToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            serverError.observe(this@ReserveConfirmActivity, Observer { errorMsg ->
                showToast(ToastType.ERROR, errorMsg)
            })
            status.observe(this@ReserveConfirmActivity, Observer {
                when (status.value) {
                    ReserveConfirmViewModel.ReservationStatus.CANCEL_SUCCESS -> ""
                    ReserveConfirmViewModel.ReservationStatus.CANCEL_FAIL -> ""
                }
            })
            plateModel.observe(this@ReserveConfirmActivity, Observer {
                setReservationTicketName(it.plateReservation.name)
                setReservationTicketDate(it.plateReservation.startDate)
                setReservationTicketTime(it.plateReservation.startDate, it.plateReservation.endDate)
                // setReservationPolicy(it.policy) // 정책 어떻게 올려나?
            })
            lessonModel.observe(this@ReserveConfirmActivity, Observer {
                setReservationTicketName(it.lessonReservation.name)
                setReservationTicketDate(it.lessonReservation.startDate)
                setReservationTicketTime(it.lessonReservation.startDate, it.lessonReservation.endDate)
            })
//            cancelSuccess.observe(this@ReserveConfirmActivity, Observer {
//                goReservationCancelSuccess()
//            })
//            cancelFail.observe(this@ReserveConfirmActivity, Observer {
//                showToast(ToastType.ERROR, getString(R.string.reserve_confirm_cancel_fail))
//            })
        }
    }

    private fun initView() {
        initToolbar(
            reserve_confirm_toolbar.toolbar,
            getString(R.string.reserve_confirm_toolbar_title),
            true
        )

        when (reservationType) {
            "ReservationPlateFromAlram" -> {
                Log.d(TAG, "reservationPlate: $plateId")
                viewModel.getReservationPlateDetail(plateId)
                initBottomView(
                    binding.reserveConfirmBottomView.bottom_layout,
                    getString(R.string.reserve_confirm_cancel_bottom_title),
                    BottimViewType.CANCEL_TYPE
                )
            }
            "ReservationLessonFromAlram" -> {
                Log.d(TAG, "reservationLesson: $lessonId")
                viewModel.getReservationLessonDetail(lessonId)
                initBottomView(
                    binding.reserveConfirmBottomView.bottom_layout,
                    getString(R.string.reserve_confirm_cancel_bottom_title),
                    BottimViewType.CANCEL_TYPE
                )
            }
            "cancel_plate" -> {
                Log.d(TAG, "cancel_plate: $plateId")
                viewModel.getReservationPlateDetail(plateId)
                initBottomView(
                    binding.reserveConfirmBottomView.bottom_layout,
                    getString(R.string.reserve_confirm_cancel_bottom_title),
                    BottimViewType.CANCEL_TYPE
                )
            }
            "cancel_lesson" -> {
                Log.d(TAG, "cancel_lesson: $lessonId")
                viewModel.getReservationLessonDetail(lessonId)
                binding.tvTicketPolicy.text = getString(R.string.reserve_confirm_lesson_policy)
                initBottomView(
                    binding.reserveConfirmBottomView.bottom_layout,
                    getString(R.string.reserve_confirm_cancel_bottom_title),
                    BottimViewType.CANCEL_TYPE
                )
            }
            "reservation" -> {
                // 뷰모델에서 예약확인 할때 ticketTime에서 id 뿌려주면됨
                Log.d(TAG, "reservation:")
                initBottomView(
                    binding.reserveConfirmBottomView.bottom_layout,
                    getString(R.string.reserve_confirm_bottom_title),
                    BottimViewType.DEFAULT_TYPE
                )
                setReservationTicketName(ticket.name)
                setReservationTicketDate(ticketTime.startDate)
                setReservationTicketTime(ticketTime.startDate, ticketTime.endDate)
            }
        }
    }

//    private fun reservationView() {
//        initBottomView(binding.reserveConfirmBottomView.bottom_layout, getString(R.string.reserve_confirm_bottom_title), BottimViewType.DEFAULT_TYPE)
//        datesList = setTimeDate(ticketTime.startDate, ticketTime.endDate)
//        with(binding) {
//            tvTicketName.text = ticket.name
//            tvTicketDate.text = datesList[0]
//            tvTicketTime.text = datesList[1]
//        }
//
//        if (ticketTime.plateAvailabilityId != null) {
//            viewModel.requestPolicy()
//            viewModel.plateAvailabilityId = ticketTime.plateAvailabilityId
//        } else {
//            binding.tvTicketPolicy.text = getString(R.string.reserve_confirm_lesson_policy)
//            viewModel.lessonAvailabilityId = ticketTime.lessonAvailabilityId
//        }
//        viewModel.ticketId = ticket.id
//    }

//    private fun cancelView() {
//        initBottomView(binding.reserveConfirmBottomView.bottom_layout, getString(R.string.reserve_confirm_cancel_bottom_title), BottimViewType.CANCEL_TYPE)
//        datesList = setTimeDate(reservation.startDate, reservation.endDate)
//        with(binding) {
//            tvTicketName.text = reservation.name
//            tvTicketDate.text = datesList[0]
//            tvTicketTime.text = datesList[1]
//        }
//    }

//    private fun setTimeDate(startTime: String, endTime: String): List<String> {
//        val dates = mutableListOf<String>()
//
//        try {
//            val nowDate = convertTime(startTime).trim().split(":")
//            val nowTime = convertTime(endTime).trim().split(":")
//
//            dates.add("${nowDate[0]}. ${nowDate[1]}. ${nowDate[2]}")
//            dates.add("${nowDate[3]}:${nowDate[4]} ~ ${nowTime[3]}:${nowTime[4]}")
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return dates
//    }

    private fun setReservationTicketName(name: String) {
        binding.tvTicketName.text = name
    }

    private fun setReservationTicketDate(startTime: String) {
        Log.d(TAG, "setReservationTicketDate: $startTime")
        val date = convertTime(startTime).trim().split(":")

        binding.tvTicketDate.text = "${date[0]}. ${date[1]}. ${date[2]}"
    }

    private fun setReservationTicketTime(startTime: String, endTime: String) {
        val start = convertTime(startTime).trim().split(":")
        val end = convertTime(endTime).trim().split(":")

        binding.tvTicketTime.text =
            "${start[3]}:${start[4]} ~ ${end[3]}:${end[4]}"
    }

    private fun setReservationPolicy(policy: String) {
        binding.tvTicketPolicy.text = policy
    }

    private fun goReserveSuccessPlate(plate: ReservationPlate) {
//        val plateIntent = Intent(this, ReserveSuccessActivity::class.java)
//            .apply {
//                putExtra(RESERVATION_PLATE, plate)
//                putExtra(CONFIRM_TYPE, confirmType)
//            }
//        startActivity(plateIntent)
    }

    private fun goReserveSuccessLesson(lesson: ReservationLesson) {
//        val lessonIntent = Intent(this, ReserveSuccessActivity::class.java)
//            .apply {
//                putExtra(RESERVATION_LESSON, lesson)
//                putExtra(CONFIRM_TYPE, confirmType)
//            }
//        startActivity(lessonIntent)
    }

    private fun goReservationCancelSuccess() {
//        val cancelIntent = Intent(this, ReserveSuccessActivity::class.java)
//            .apply {
//                putExtra(CONFIRM_TYPE, confirmType)
//            }
//        startActivity(cancelIntent)
//        finish()
    }

    override fun bottomAction() {
        super.bottomAction()

//        when (confirmType) {
//            "reservation" -> {
//                if (ticketTime.plateAvailabilityId != null) {
//                    viewModel.reservationPlate()
//                } else {
//                    viewModel.reservationLesson()
//                }
//            }
//            "cancel_lesson" -> {
//                viewModel.reservationLessonCancel(reservation.id)
//            }
//            "cancel_plate" -> {
//                viewModel.reservationPlateCancel(reservation.id)
//            }
//        }
    }

}