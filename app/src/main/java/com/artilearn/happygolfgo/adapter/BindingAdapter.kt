package com.artilearn.happygolfgo.adapter

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.util.convertTime

//@BindingAdapter("plateNumber")
//fun plateNumber(text: TextView, url: Int) {
//    url.let {
//        text.text = url.toString()
//    }
//}
//
//@BindingAdapter("plateReserver")
//fun plateReserve(text: TextView, url: Int) {
//    when (url) {
//        0 -> { // negative reserve
//            text.text = "예약중"
//            text.setTextColor(ContextCompat.getColor(text.context, R.color.color_dark_red))
//        }
//        1 -> { // ok reserve
//            text.text = "예약가능"
//            text.setTextColor(ContextCompat.getColor(text.context, R.color.color_black))
//        }
//    }
//}

@BindingAdapter("setCreateAt", "setName")
fun lessonReviewListData(tv: TextView, date: String, name: String) {
    val lessonDate = convertTime(date).trim().split(":")
    //val conver = "yyyy:MM:dd:HH:mm"
    //tools:text="2020. 05. 30 / OOO 코치"

    tv.text = "${lessonDate[0]}. ${lessonDate[1]}. ${lessonDate[2]} / $name"
}