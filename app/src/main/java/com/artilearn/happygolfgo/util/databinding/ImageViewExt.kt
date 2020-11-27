package com.artilearn.happygolfgo.util.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.artilearn.happygolfgo.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("onBindProfile")
fun ImageView.bindImage(imagePath: String?) {
    if (imagePath != null) {
        Glide.with(this)
            .load(imagePath)
            .apply(RequestOptions.bitmapTransform(
                MultiTransformation(
                    CenterCrop(),
                    CircleCrop()
                )
            ))
            .into(this)
    } else {
        Glide.with(this)
            .load(R.drawable.ic_empty_user_profile)
            .into(this)
    }
}