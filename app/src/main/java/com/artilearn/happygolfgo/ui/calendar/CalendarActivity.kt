package com.artilearn.happygolfgo.ui.calendar

import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.TICKET
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.ActivityCalendarBinding
import com.artilearn.happygolfgo.enummodel.CommonFragmentType
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class CalendarActivity : BaseActivity<ActivityCalendarBinding>(R.layout.activity_calendar) {

    private var ticket: Ticket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        ticket = intent.getParcelableExtra(TICKET)
        initToolbar(calendar_toolbar.toolBar, getString(R.string.calendar_toolbar_title), true)
        showCommonFragment(R.id.calendarFrameLayout, null, CommonFragmentType.CALENDAR, ticket)
    }
}