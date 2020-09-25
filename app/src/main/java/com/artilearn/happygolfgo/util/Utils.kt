package com.artilearn.happygolfgo.util

import android.content.Context
import android.graphics.Color
import android.media.MediaDrm
import android.media.UnsupportedSchemeException
import android.os.Build
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.app.NotificationManagerCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.artilearn.happygolfgo.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("확인") { snackbar.dismiss() }
        snackbar.setTextColor(Color.RED)
    }.show()
}

fun convertTime(time: String): String {
    val default = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val conver = "yyyy:MM:dd:HH:mm"

    val input = SimpleDateFormat(default, Locale.KOREA)
    input.timeZone = TimeZone.getTimeZone("UTC")

    val output = SimpleDateFormat(conver, Locale.KOREA)
    val date = input.parse(time)

    return output.format(date)
}

fun convertDate(str: String): Date? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.KOREA)
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return try {
        dateFormat.parse(str)
    } catch (e: ParseException) {
        null
    }
}

fun convertCalendar(date: Date): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = date

    return calendar
}

fun notificationCheck(context: Context): Int {
    val check = NotificationManagerCompat.from(context).areNotificationsEnabled()

    return if (check) 1
    else 0
}

fun loading(progressbar: ProgressBar, loading: Boolean) {
    progressbar.visibility = if (loading) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

fun imageLoadingProgress(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.setImage(iv: ImageView, str: String?) {
    str?.let {
        Glide.with(iv).load(it)
            .apply(
                RequestOptions()
                    .placeholder(imageLoadingProgress(iv.context))
                    .error(R.drawable.ic_error)
            )
            .into(iv)
    }
}

fun convertStringtoLong(time: String): Long {
    val defaultFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    defaultFormat.timeZone = TimeZone.getTimeZone("UTC")

    val longTime = defaultFormat.parse(time)

    return longTime.time
}

fun Date.convertDateTime(): String {
    return when (val diff = (Date().time - this.time) / 1000) {
        in 0 until 10 -> "지금 막"
        in 10 until  60 -> "${diff}초 전"
        in 60 until  60 * 60 -> "${diff / 60}분 전"
        in 60 * 60 until 60 * 60 * 24 -> "${diff / (60 * 60)}시간 전"
        in 60 * 60 * 24 until 60 * 60 * 48 -> "어제"
        in 60 * 60 * 48 until 60 * 60 * 24 * 7 -> "${diff / (60 * 60 * 24)}일 전"
        else -> SimpleDateFormat("yyy.MM.dd", Locale.getDefault()).format(this)
    }
}