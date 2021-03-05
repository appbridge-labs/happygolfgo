package com.artilearn.happygolfgo.util.databinding

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("onClick")
fun View.bindOnClick(listener: View.OnClickListener) {
    setOnClickListener(listener)
}