package com.artilearn.happygolfgo.util.databinding

import android.view.View
import androidx.databinding.BindingAdapter
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.util.convertCalendar
import com.artilearn.happygolfgo.util.convertDate
import com.artilearn.happygolfgo.util.convertTime
import java.util.*

@BindingAdapter("bindAvailablie", "bindEvent")
fun CalendarView.bindDays(available: List<String>?, events: List<String>?) {
    val eventDays: MutableList<Calendar>

    this.setMinimumDate(minimumDate())
    this.setMaximumDate(maximumDate())

    available?.let {
        val disabled = setServerDates(it)
        this.setDisabledDays(disabled)
    }

    events?.let {
        eventDays = arrayListOf()

        for (i in events.indices) {
            val date = convertDate(events[i])
            val calendar = if (date != null) {
                convertCalendar(date)
            } else {
                null
            }

            calendar?.let { eventDays.add(it) }
        }

        this.setEvents(setDots(eventDays))
    }


    this.setOnDayClickListener(object : OnDayClickListener {
        override fun onDayClick(eventDay: EventDay) {

        }
    })

    this.visibility = View.VISIBLE
}

private fun minimumDate(): Calendar {
    return Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, -1)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

private fun maximumDate(): Calendar {
    return Calendar.getInstance().apply {
        add(Calendar.MONTH, 1)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

private fun setServerDates(list: List<String>): List<Calendar> {
    val calendarDays = mutableListOf<CalendarDay>()
    val calendars = mutableListOf<Calendar>()
    val min = minimumDate()
    val max = maximumDate()

    val longTime = (max.timeInMillis - min.timeInMillis) / 1000
    val diff = longTime / (24 * 60 * 60)

    for (i in 0 until diff.toInt()) {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, i)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        calendarDays.add(CalendarDay(i, calendar))
    }

    for (i in list.indices) {
        val str = convertTime(list[i])
        val strSub = str.split(":")
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, strSub[0].toInt())
            set(Calendar.MONTH, strSub[1].toInt().minus(1))
            set(Calendar.DAY_OF_MONTH, strSub[2].toInt())
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        calendars.add(calendar)
    }

    return calendarDays.filterNot { c ->
        c.calendar in calendars
    }
        .map { it.calendar }
}

private fun setDots(event: List<Calendar>?): List<EventDay> {
    val eventDays = mutableListOf<EventDay>()

    event?.let {
        for (i in it.indices) {
            eventDays.add(EventDay(it[i], R.drawable.ic_calendar_event))
        }
    }

    return eventDays
}

data class CalendarDay(val id: Int, val calendar: Calendar)