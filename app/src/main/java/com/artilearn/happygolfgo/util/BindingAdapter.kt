package com.artilearn.happygolfgo.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.Alram
import com.artilearn.happygolfgo.data.LessonLog
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.ui.alram.AlramAdapter
import com.artilearn.happygolfgo.ui.home.HomeAdapter
import com.artilearn.happygolfgo.ui.lessonpostlist.LessonPostListAdapter
import com.artilearn.happygolfgo.ui.tickettime.TicketTimeAdapter
import java.util.*

@BindingAdapter("setTicketStartDate", "setTicketEndDate")
fun ticketDate(tv: TextView, startDate: String, endDate: String) {
    val startTime = convertTime(startDate).trim().split(":")
    val endTime = convertTime(endDate).trim().split(":")

    tv.text = "${startTime[1]}.${startTime[2]}~${endTime[1]}.${endTime[2]}"
}

@BindingAdapter(
    "setTicketTotalCount",
    "setTicketRemainingCount",
    "setTicketIsUnLimited",
    "setTicketOnlyOnceToday"
)
fun ticketCount(
    tv: TextView,
    total: Int,
    remaining: Int,
    isUnlimited: Boolean,
    onceToday: Boolean
) {
    if (isUnlimited && onceToday) {
        tv.text = "1일 1회 사용"
    } else if (isUnlimited && !onceToday) {
        tv.text = "무제한 사용"
    } else {
        tv.text = "${total}회 중 ${remaining}회 남음"
    }
}

@BindingAdapter("setTickets")
fun RecyclerView.setTicketItems(tickes: List<Ticket>?) {
    with(adapter as HomeAdapter) {
        this.clear()
        tickes?.let { this.setItems(it) }
    }
}

@BindingAdapter("setLessonLog")
fun RecyclerView.setLessonLogItems(logs: List<LessonLog>?) {
    with(adapter as LessonPostListAdapter) {
        this.clear()
        logs?.let { this.setItems(it) }
    }
}

@BindingAdapter("setLogTime", "setLogCoachName")
fun setLessonLogItem(tv: TextView, time: String, coach: String) {
    val logTime = convertTime(time).trim().split(":")

    tv.text = "${logTime[0]}. ${logTime[1]}. ${logTime[2]} / $coach"
}

@BindingAdapter("setAlramMessage")
fun RecyclerView.setAlramMessageItems(message: List<Alram>?) {
    with(adapter as AlramAdapter) {
        this.clear()
        message?.let { setItems(it) }
    }
}

@BindingAdapter("setAlramDate")
fun setAlramDate(tv: TextView, time: String) {
    val long = convertStringtoLong(time)
    val realTime = Date(long).convertDateTime()
    tv.text = realTime
}

@BindingAdapter("setAlramTitle")
fun setAlramTitle(tv: TextView, title: String?) {
    title?.let {
        tv.text = it
    }
}

@BindingAdapter("setAlramBody")
fun setAlramBody(tv: TextView, body: String?) {
    body?.let {
        tv.text = it
    }
}

@BindingAdapter("setTicketTime")
fun RecyclerView.setTicketTimeItem(ticketTime: List<TicketTime>?) {
    with(adapter as TicketTimeAdapter) {
        this.clear()
        ticketTime?.let { setItems(it) }
    }
}

@BindingAdapter("setTicketTimeStart", "setTicketTimeEnd")
fun setTicketTimeDate(tv: TextView, start: String, end: String) {
    val startTime = convertTime(start).trim().split(":")
    val endTime = convertTime(end).trim().split(":")

    tv.text = "${startTime[3]}:${startTime[4]}~${endTime[3]}:${endTime[4]}"
}

@BindingAdapter("setTicketLessonStatus", "setTicketPlateAllCount")
fun setTicketTimeTitle(tv: TextView, lessonStauts: Int?, plateAllCount: Int?) {
    lessonStauts?.let {
        tv.text = "상태"
    }
    plateAllCount?.let {
        tv.text = "총 ${plateAllCount}석"
    }
}

@BindingAdapter("setTicketLessonNowStatus", "setTicketPlateAvailableCount")
fun setTicketTimeDetail(tv: TextView, lessonStatus: Int?, plateAvailableCount: Int?) {
    lessonStatus?.let {
        tv.text = "예약가능"
        tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_black))
    }

    plateAvailableCount?.let {
        if (it != 0) {
            tv.text = "잔여 ${it}석"
            tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_black))
        }
    }
}

@BindingAdapter("setTicketName")
fun setTicketName(tv:TextView, name: String) {
    tv.text = name
}

@BindingAdapter("setReservationDate")
fun setReservationStartTime(tv: TextView, time: String) {
    val start = convertTime(time).trim().split(":")
    tv.text = "${start[1]}.${start[2]}"
}

@BindingAdapter("setTicketStatus")
fun setReservationStatus(tv: TextView, status: Int) {
    when (status) {
        0 -> tv.text = "예약완료"
        1 -> tv.text = "1번"
        2 -> tv.text = "2번"
        3 -> tv.text = "3번"
        else -> tv.text = "else"
    }
}

@BindingAdapter("setLogThumbNail")
fun setLogThumbNail(iv: ImageView, thumbNail: String?) {
    thumbNail?.let {
        iv.setImage(iv, it)
    }
}

@BindingAdapter("setLogComment")
fun setLogComment(tv: TextView, comment: String?) {
    comment?.let {
        tv.text = it
    }
}
