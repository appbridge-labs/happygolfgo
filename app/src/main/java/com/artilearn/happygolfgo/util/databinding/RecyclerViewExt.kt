package com.artilearn.happygolfgo.util.databinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.ui.home.gamecenter.GameCenterAdapter
import com.artilearn.happygolfgo.ui.home.gamecenter.model.GCRankModel

@BindingAdapter("list")
fun RecyclerView.bindList(list: List<GCRankModel>?) {
    with(adapter as GameCenterAdapter) {
        this.clear()
        list?.let { this.setItems(it) }
    }
}