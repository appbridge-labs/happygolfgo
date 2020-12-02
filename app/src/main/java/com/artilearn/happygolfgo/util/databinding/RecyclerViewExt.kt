package com.artilearn.happygolfgo.util.databinding

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.data.golfgame.GolfGameResponse
import com.artilearn.happygolfgo.data.golfrank.GolfRankResponse
import com.artilearn.happygolfgo.ui.golfgame.GolfGameAdapter
import com.artilearn.happygolfgo.ui.golfgame.model.GolfGameModel
import com.artilearn.happygolfgo.ui.golfrank.GolfRankAdapter
import com.artilearn.happygolfgo.ui.golfrank.model.GolfRankModel
import com.artilearn.happygolfgo.ui.home.gamecenter.GameCenterAdapter
import com.artilearn.happygolfgo.ui.home.gamecenter.model.GCRankModel

@BindingAdapter("list")
fun RecyclerView.bindList(list: List<GCRankModel>?) {
    with(adapter as GameCenterAdapter) {
        this.clear()
        list?.let { this.setItems(it) }
    }
}

@BindingAdapter("onRankList")
fun RecyclerView.bindRankList(list: List<GolfRankModel>?) {
    with(adapter as GolfRankAdapter) {
        this.clear()
        list?.let { this.setItems(it) }
    }
}

@BindingAdapter("onGolfGameList")
fun RecyclerView.bindGolfGameList(list: List<GolfGameModel>?) {
    with(adapter as GolfGameAdapter) {
        this.clear()
        list?.let { this.setItems(it) }
    }
}