package com.artilearn.happygolfgo.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.models.Ticket
import com.artilearn.happygolfgo.ui.BaseActivity
import com.artilearn.happygolfgo.util.convertCalendar
import com.artilearn.happygolfgo.util.convertDate
import com.artilearn.happygolfgo.util.noticeDialog
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult
import kotlinx.android.synthetic.main.activity_select_date.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*
import java.util.*

class SelectDateActivity : BaseActivity() {

    private var date: String? = null
    private var dayZone: String? = null
    private var availableDates: List<Calendar> = listOf()
    private var calendarMin: Calendar? = null
    private var calendarMax: Calendar? = null

    private var eventList: ArrayList<Calendar> = arrayListOf()

    private lateinit var requestManager: RequestManager
    private lateinit var userManager: HappyGolfGoUserManager
    private lateinit var request: HappyGolfGoRequest
    private lateinit var ticket: Ticket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_date)

        setFrameLayoutID(R.id.date_frameLayout)
        setBaseFragments()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::requestManager.isInitialized) requestManager.rootActivity.finish()
    }

    private fun initView() {
        ticket = intent.getSerializableExtra("ticket") as Ticket
        requestManager = RequestManager(this)
        userManager = HappyGolfGoUserManager(this)

        when (ticket.type.integer) {
            0 -> {
                setUpActionBar(
                    toolbar_select.toolbar,
                    getString(R.string.lessondatechoose),
                    true
                )
            }
            2 -> {
                setUpActionBar(
                    toolbar_select.toolbar,
                    getString(R.string.platedatechoose),
                    true
                )
            }
        }

        val current = Calendar.getInstance()
        calendarview.setDate(current)
        requestData()

        calendarListener()
    }

    private fun calendarListener() {
        calendarview.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                if (eventList.contains(eventDay.calendar)) {
                    noticeDialog(
                        calendarview.context,
                        "날짜 선택",
                        "당일 중복 예약이 불가능한 이용권 입니다."
                    )
                } else {
                    if (eventDay.isEnabled) {
                        noticeDialog(
                            calendarview.context,
                            "날짜 선택",
                            "선택하신 날짜는 예약이 불가능 합니다."
                        )
                    } else {
                        val month = eventDay.calendar.get(Calendar.MONTH)
                        date = "startDate=${eventDay.calendar.get(Calendar.YEAR)}-${month + 1}-${eventDay.calendar.get(Calendar.DAY_OF_MONTH)}"
                        dayZone = date + "&" + "ticketID=${ticket.id}"
                        startActivity()
                    }
                }
            }
        })
    }

    private fun addCalendarEvent() {
        val list = mutableListOf<EventDay>()

        if (eventList.size != 0) {
            for (i in 0 until eventList.size) {
                list.add(EventDay(eventList[i], R.drawable.ic_calendar_event))
            }
            runOnUiThread { calendarview.setEvents(list) }
        }

        runOnUiThread {
            hideLoading()
            availableDates.let { calendarview.selectedDates = it }
            calendarMin?.let {calendarview.setMinimumDate(it) }
            calendarMax?.let { calendarview.setMaximumDate(it) }
            calendarview.visibility = View.VISIBLE
        }
    }

    private fun startActivity() {
        val intent = Intent(this, SelectTimeActivity::class.java)
        intent.putExtra("ticket", ticket)
        intent.putExtra("idx", dayZone)
        startActivity(intent)
    }

    private fun requestData() {
        calendarview.visibility = View.INVISIBLE
        showLoading()

        when (ticket.type.integer) {
            0 -> {
                request = HappyGolfGoRequest.selectLessonDate
                request.endpoint = ticket.id.toString()
            }
            2 -> {
                request = HappyGolfGoRequest.selectPlateDate
                request.endpoint = ticket.id.toString()
            }
        }

        http(request)
    }

    private fun emptyRequestData() {
        val day = Calendar.getInstance()

        val firstDay = day.getActualMaximum(Calendar.DAY_OF_MONTH)
        val lastDay = day.getMinimum(Calendar.DAY_OF_MONTH)

        val firstCalendar = Calendar.getInstance()
        val lastCalendar = Calendar.getInstance()
        firstCalendar.set(day.get(Calendar.YEAR), day.get(Calendar.MONTH) -1, firstDay)
        lastCalendar.set(day.get(Calendar.YEAR), day.get(Calendar.MONTH) -1, lastDay)

        calendarMin = firstCalendar
        calendarMax = lastCalendar
    }

    private fun http(body: HappyGolfGoRequest) {
        requestManager.request(body, userManager.user) { result, json ->

            when (result) {
                RequestResult.nSuccess -> {
                    runOnUiThread {
                        showUnknownError()
                        hideLoading()
                    }
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        201 -> {
                            val availableDate = json?.get("availableDates") as ArrayList<String>
                            val eventDate = json["eventDates"] as ArrayList<String>

                            if (availableDate.size != 0) {
                                val min = Collections.min(availableDate)
                                val max = Collections.max(availableDate)
                                val minDate = convertDate(min)
                                val maxDate = convertDate(max)

                                availableDates = availableDate.map {
                                    val date = convertDate(it)
                                    convertCalendar(date!!)
                                }

                                minDate?.let { calendarMin = convertCalendar(it) }
                                maxDate?.let { calendarMax = convertCalendar(it) }
                            } else {
                                runOnUiThread {
                                    emptyRequestData()
                                    noticeDialog(this, "날짜 선택", "예약 가능한 날짜가 없습니다.")
                                }
                            }

                            if (eventDate.size != 0) {
                                for (i in 0 until eventDate.size) {
                                    val date = convertDate(eventDate[i])
                                    eventList.add(convertCalendar(date!!))
                                }
                            }

                            addCalendarEvent()
                        }
                    }
                }
                RequestResult.failure -> {
                    runOnUiThread {
                        showNetworkFailure()
                        hideLoading()
                    }
                }
                else -> {
                    runOnUiThread {
                        showUnknownError()
                        hideLoading()
                    }
                }
            }
        }
    }
}
