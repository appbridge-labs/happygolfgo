package com.artilearn.happygolfgo.util

import android.content.Context
import android.graphics.Color
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