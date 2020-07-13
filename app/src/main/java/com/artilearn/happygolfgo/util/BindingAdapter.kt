package com.artilearn.happygolfgo.util

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("setTicketStartDate", "setTicketEndDate")
fun ticketDate(tv: TextView, startDate: String, endDate: String) {
    Log.d("binding", "ticketDate: $startDate, $endDate")
}

@BindingAdapter("setTicketTotalCount", "setTicketRemainingCount")
fun ticketCount(tv: TextView, total: Int, remaining: Int) {
    Log.d("binding", "ticketCount: $total, $remaining")
}

