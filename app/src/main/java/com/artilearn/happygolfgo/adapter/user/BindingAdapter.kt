package com.artilearn.happygolfgo.adapter.user

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.artilearn.happygolfgo.R

@BindingAdapter("plateNumber")
fun plateNumber(text: TextView, url: Int) {
    url.let {
        text.text = url.toString()
    }
}

@BindingAdapter("plateReserver")
fun plateReserve(text: TextView, url: Int) {
    when (url) {
        0 -> { // negative reserve
            text.text = "예약중"
            text.setTextColor(ContextCompat.getColor(text.context, R.color.color_dark_red))
        }
        1 -> { // ok reserve
            text.text = "예약가능"
            text.setTextColor(ContextCompat.getColor(text.context, R.color.color_black))
        }
    }
}