package com.artilearn.happygolfgo.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.data.Alram
import com.artilearn.happygolfgo.data.LessonLog
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.ui.alram.AlramAdapter
import com.artilearn.happygolfgo.ui.home.HomeAdapter
import com.artilearn.happygolfgo.ui.lessonlog.LessonLogAdapter
import kotlin.math.log

@BindingAdapter("setTicketStartDate", "setTicketEndDate")
fun ticketDate(tv: TextView, startDate: String, endDate: String) {
    val startTime = convertTime(startDate).trim().split(":")
    val endTime = convertTime(endDate).trim().split(":")

    tv.text = "${startTime[1]}.${startTime[2]}~${endTime[1]}.${endTime[2]}"
}

@BindingAdapter("setTicketTotalCount", "setTicketRemainingCount")
fun ticketCount(tv: TextView, total: Int, remaining: Int) {
    tv.text = "${total}회 중 ${remaining}회 남음"
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
    with(adapter as LessonLogAdapter) {
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
    val timeList = convertTime(time).trim().split(":")
    tv.text = "${timeList[0]}.${timeList[1]}.${timeList[2]} ${timeList[3]}:${timeList[4]}"
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