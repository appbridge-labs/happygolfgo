package com.artilearn.happygolfgo.util

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtil {
    companion object {
        fun getConnectivityStatus(context: Context): Int {
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetwork
            return if (activeNetwork != null) 1 else 0
        }
    }
}