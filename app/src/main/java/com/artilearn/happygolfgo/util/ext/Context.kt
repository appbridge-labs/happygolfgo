package com.artilearn.happygolfgo.util.ext

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

fun Context.setDialogSize(dialog: Dialog) {
    val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)

    val params: ViewGroup.LayoutParams? = dialog.window!!.attributes
    val deviceWidth = size.x
    params?.width = (deviceWidth * 0.8).toInt()
    dialog.window?.attributes = params as WindowManager.LayoutParams
}

fun Context.showKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, 0)
}

fun Context.hideKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}