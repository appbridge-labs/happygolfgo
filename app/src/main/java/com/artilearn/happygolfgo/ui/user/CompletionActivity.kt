package com.artilearn.happygolfgo.ui.user

import android.content.Intent
import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.models.LessonReservation
import com.artilearn.happygolfgo.models.PlateReservation
import com.artilearn.happygolfgo.ui.BaseActivity
import com.artilearn.happygolfgo.util.convertTime
import kotlinx.android.synthetic.main.activity_completion.*
import kotlinx.android.synthetic.main.partial_bottom_button.view.*

class CompletionActivity : BaseActivity() {

    private lateinit var type: Any
    private lateinit var plateReservation: PlateReservation
    private lateinit var lessonReservation: LessonReservation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completion)

        initListener()
        initData()
    }

    private fun initData() {
        bottom_nav.text_view_title_bottom_button.text = "확인"
        val itemCheck = intent.getSerializableExtra("item")

        itemCheck?.let { type = it }

        when (intent.getIntExtra("data", 0)) {
            0 -> {
                tv_title.text = getString(R.string.text_view_title_cancel_reservation)
                tv_content.text = getString(R.string.text_view_subtitle_cancel_reservation)
            }
            1 -> {
                typeSuccess(type)
            }
        }
    }

    private fun typeSuccess(type: Any) {
        when (type) {
            is PlateReservation -> {
                plateReservation = type
                val start = convertTime(plateReservation.startDate).trim().split(":")
                val end = convertTime(plateReservation.endDate).trim().split(":")
                val time = "${start[0]}년 ${start[1]}월 ${start[2]}일 ${start[3]}:${start[4]} ~ ${end[3]}:${end[4]}"
                val plate = "${plateReservation.plateNumber}번 타석"

                if (plateReservation.totalCount != 0) {
                    val use =
                        "연습장 이용권 (${plateReservation.totalCount}회 중 ${plateReservation.remainingCount}회 남음)"
                    tv_title.text = plateReservation.name
                    tv_content.text = "${use} \n ${time} / ${plate}"
                } else {
                    val use = "연습장 이용권 (무제한 사용권)"
                    tv_title.text = plateReservation.name
                    tv_content.text = "${use} \n ${time} / ${plate}"
                }
            }
            is LessonReservation -> {
                lessonReservation = type
                val start = convertTime(lessonReservation.startDate).trim().split(":")
                val end = convertTime(lessonReservation.endDate).trim().split(":")
                val time =
                    "${start[0]}년 ${start[1]}월 ${start[2]}일 ${start[3]}:${start[4]} ~ ${end[3]}:${end[4]}"

                if (lessonReservation.totalCount != 0) {
                    val use = "연습장 이용권 (${lessonReservation.totalCount}회 중 ${lessonReservation.remainingCount}회 남음)"
                    tv_title.text = lessonReservation.name
                    tv_content.text = "${use} \n ${time}"
                } else {
                    val use = "연습장 이용권 (무제한 사용권)"
                    tv_title.text = lessonReservation.name
                    tv_content.text = "${use} \n ${time}"
                }
            }
        }
    }

    private fun initListener() {
        bottom_nav.setOnClickListener { startActivity() }
    }

    private fun startActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
