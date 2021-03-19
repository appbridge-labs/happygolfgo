package com.artilearn.happygolfgo.util.databinding

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.artilearn.happygolfgo.R

@BindingAdapter("onRank", "onIsMe")
fun TextView.bindRank(rank: String, isMe: Boolean) {
    if (isMe) {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_green))
    } else {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_lucky_gray))
    }
    this.text = rank
}

@BindingAdapter("onNickName", "onIsMe")
fun TextView.bindNickName(nickName: String?, isMe: Boolean) {
    if (isMe) {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_green))
    } else {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_lucky_gray))
    }

    this.text = nickName ?: "닉네임 없음"
}

@BindingAdapter("onAverages", "onIsMe")
fun TextView.bindAverages(averages: String, isMe: Boolean) {
    if (isMe) {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_green))
    } else {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.color_lucky_gray))
    }
    this.text = averages
}

@BindingAdapter("pauseList")
fun TextView.pauseTicketText(item: List<String>?) {
    val sb = StringBuilder()

    item?.let {
        for( i in item.indices) {
            if (item.lastIndex == i) {
                sb.append(item[i])
            } else {
                sb.append("${item[i]}\n")
            }
        }

        text = sb
    }
}