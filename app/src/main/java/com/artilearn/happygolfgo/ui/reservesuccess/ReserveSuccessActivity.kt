package com.artilearn.happygolfgo.ui.reservesuccess

import android.content.Intent
import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.RESERVATION_LESSON
import com.artilearn.happygolfgo.constants.RESERVATION_PLATE
import com.artilearn.happygolfgo.data.ReservationLesson
import com.artilearn.happygolfgo.data.ReservationPlate
import com.artilearn.happygolfgo.databinding.ActivityReserveSuccessBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.home.HomeActivity
import com.artilearn.happygolfgo.util.convertTime
import kotlinx.android.synthetic.main.activity_reserve_success.*
import kotlinx.android.synthetic.main.custom_bottom_view.view.*

class ReserveSuccessActivity :
    BaseActivity<ActivityReserveSuccessBinding>(R.layout.activity_reserve_success) {
    private val TAG = javaClass.simpleName
    private val reservationPlate by lazy { intent.getParcelableExtra<ReservationPlate>(RESERVATION_PLATE) }
    private val reservationLesson by lazy { intent.getParcelableExtra<ReservationLesson>(RESERVATION_LESSON) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        setText()
    }

    private fun initView() {
        initBottomView(
            reserve_success_bottom_view.bottomLayout,
            getString(R.string.reserve_success_bottom_title),
            BottimViewType.DEFAULT_TYPE
        )
    }

    private fun setText() {
        with(binding) {
            reservationPlate?.let { plate ->
                showToast(ToastType.SUCCESS, "$plate")
                tvTitle.text = convertToTitle(plate.name, plate.totalCount, plate.remainingCount)
                tvSubtitle.text = convertToTime(plate.startDate, plate.endDate)
            }
            reservationLesson?.let { lesson ->
                tvTitle.text = convertToTitle(lesson.name, lesson.totalCount, lesson.remainingCount)
                tvSubtitle.text = convertToTime(lesson.startDate, lesson.endDate)
            }
        }
    }

    private fun convertToTitle(name: String, allCount: Int, count: Int): String {
        return "$name ( ${allCount}회 중 ${count}남음 )"
    }

    private fun convertToTime(start: String, end: String): String {
        val startTime = convertTime(start).trim().split(":")
        val endTime = convertTime(end).trim().split(":")

        return "${startTime[0]}년 ${startTime[1]}월 ${startTime[2]}일 " +
                "${startTime[3]}:${startTime[4]} ~ ${endTime[3]}:${endTime[4]}"
    }

    override fun bottomAction() {
        super.bottomAction()
        val homeIntent = Intent(this, HomeActivity::class.java)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        startActivity(homeIntent)
    }

}