package com.artilearn.happygolfgo.util.databinding

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.artilearn.happygolfgo.R

@BindingAdapter("onRank", "onIsMe")
fun TextView.bindRank(text: String, isMe: Boolean) {
    if (isMe) {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_green))
    } else {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_lucky_gray))
    }
    this.text = text
}

@BindingAdapter("onNickName", "onIsMe")
fun TextView.bindNickName(text: String, isMe: Boolean) {
    if (isMe) {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_green))
    } else {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_lucky_gray))
    }
    this.text = text
}

@BindingAdapter("onAverages", "onIsMe")
fun TextView.bindAverages(text: String, isMe: Boolean) {
    if (isMe) {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_green))
    } else {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_lucky_gray))
    }
    this.text = text
}