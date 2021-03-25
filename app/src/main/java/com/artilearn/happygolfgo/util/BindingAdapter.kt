package com.artilearn.happygolfgo.util

import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.Alram
import com.artilearn.happygolfgo.data.LessonLog
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.data.home.HomeResponseInfo
import com.artilearn.happygolfgo.ui.home.alram.AlramAdapter
import com.artilearn.happygolfgo.ui.home.reservation.ReservationAdapter
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter
import com.artilearn.happygolfgo.ui.home.reservation.ReservationViewModel
import com.artilearn.happygolfgo.ui.lessonpostlist.LessonPostListAdapter
import com.artilearn.happygolfgo.ui.tickettime.TicketTimeAdapter
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import java.util.*

@BindingAdapter("setTicketType")
fun setTicketType(iv: ImageView, type: Int) {
    when (type) {
        0 -> iv.setImageResource(R.drawable.ic_reservation_lesson_point)
        2 -> iv.setImageResource(R.drawable.ic_home_reservation_ticket_point)
        else -> iv.setImageResource(R.drawable.ic_home_reservation_ticket_point)
    }
}

@BindingAdapter("setTicketStartDate", "setTicketEndDate")
fun ticketDate(tv: TextView, startDate: String, endDate: String) {
    val startTime = convertTime(startDate).trim().split(":")
    val endTime = convertTime(endDate).trim().split(":")

    val text = "${startTime[1]}.${startTime[2]}~${endTime[1]}.${endTime[2]}"
    tv.text = text
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
        val text = "${total}회 중 ${remaining}회 남음"
        tv.text = text
    }
}

@BindingAdapter("setTickets", "setScroll")
fun RecyclerView.setTicketItems(tickes: List<Ticket>?, vm: ReservationViewModel) {
    with(adapter as ReservationNewAdapter) {
        tickes?.let {
            this.addItems(it)
            //this.submitList(it)
        }
    }

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (recyclerView.scrollState) {
                0 -> vm.onButtonState(true)
                else -> vm.onButtonState(false)

            }
        }
    })
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
    val text = "${logTime[0]}. ${logTime[1]}. ${logTime[2]} / $coach"
    tv.text = text
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

    val text = "${startTime[3]}:${startTime[4]}~${endTime[3]}:${endTime[4]}"
    tv.text = text
}

@BindingAdapter("setTicketLessonStatus", "setTicketPlateAllCount")
fun setTicketTimeTitle(tv: TextView, lessonStauts: Int?, plateAllCount: Int?) {
    lessonStauts?.let {
        tv.text = "상태"
    }
    plateAllCount?.let {
        val text = "총 ${plateAllCount}석"
        tv.text = text
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
            val text = "잔여 ${it}석"
            tv.text = text
            tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_black))
        } else {
            tv.text = "예약불가"
            tv.setTypeface(null, Typeface.BOLD)
            tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_red))
        }
    }
}

@BindingAdapter("setTicketName")
fun setTicketName(tv: TextView, name: String) {
    tv.text = name
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

@BindingAdapter("setReservationStartTime")
fun setReservationStartTime(tv: TextView, startTime: String) {
    val time = convertTime(startTime).trim().split(":")
    tv.text = String.format("%s:%s", time[3], time[4])
}

@BindingAdapter("setReservationEndTime")
fun setReservationEndTime(tv: TextView, endTime: String) {
    val time = convertTime(endTime).trim().split(":")
    tv.text = String.format("%s:%s", time[3], time[4])
}

@BindingAdapter("setReservationPoint")
fun setReservationType(
    iv: ImageView,
    plateNumber: Int?
) {
    if (plateNumber == null) {
        iv.setImageResource(R.drawable.ic_reservation_lesson_point)
    } else {
        iv.setImageResource(R.drawable.ic_home_reservation_ticket_point)
    }
}

@BindingAdapter("setReservationCalendarColor")
fun setReservationCalendarColor(
    cl: ConstraintLayout,
    plateNumber: Int?
) {
    if (plateNumber == null) {
        cl.setBackgroundResource(R.drawable.bg_reservation_list_calendar_lesson)
    } else {
        cl.setBackgroundResource(R.drawable.bg_reservation_list_calendar_plate)
    }
}

@BindingAdapter("setReservationMonth", "setReservationMonthType")
fun setReservationMonth(
    tv: TextView,
    startDate: String,
    plateNumber: Int?
) {
    val date = convertTime(startDate).trim().split(":")
    val month = date[1].convertMonth()
    tv.text = String.format("%s월", month)

    if (plateNumber == null) {
        tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_reservation_lesson_text_month))
    } else {
        tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_reservation_plate_text_month))
    }
}

@BindingAdapter("setReservationDay", "setReservationDayType")
fun setReservationDay(
    tv: TextView,
    startDate: String,
    plateNumber: Int?
) {
    val date = convertTime(startDate).trim().split(":")
    tv.text = String.format("%s", date[2])

    if (plateNumber == null) {
        tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_reservation_lesson_text_day))
    } else {
        tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_reservation_plate_text_day))
    }
}

@BindingAdapter("setReservationCard", "setReservationCardDay")
fun setReservationCard(
    cv: CardView,
    plateNumber: Int?,
    today: String
) {
    val currentDate = getNowDate()
    val reservationDate = convertDate(today)

    if (plateNumber == null) {
        if (currentDate.convertString() == reservationDate?.convertString()) {
            cv.setBackgroundResource(R.drawable.bg_reservation_list_card_lesson)
        } else {
            cv.setBackgroundResource(R.drawable.bg_reservation_card_default)
        }
    } else {
        if (currentDate.convertString() == reservationDate?.convertString()) {
            cv.setBackgroundResource(R.drawable.bg_reservation_list_card_plate)
        } else {
            cv.setBackgroundResource(R.drawable.bg_reservation_card_default)
        }
    }
}
