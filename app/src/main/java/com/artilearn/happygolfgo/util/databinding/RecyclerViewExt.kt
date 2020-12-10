package com.artilearn.happygolfgo.util.databinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
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
        list?.let { items ->
            this.setItems(items)
            findRankPosition(this@bindRankList, items)
        }
    }
}

@BindingAdapter("onGolfGameList")
fun RecyclerView.bindGolfGameList(list: List<GolfGameModel>?) {
    with(adapter as GolfGameAdapter) {
        this.clear()
        list?.let { items ->
            this.setItems(items)
            //findRankPosition(this@bindGolfGameList, items)
        }
    }
}

private fun <T> findRankPosition(rv: RecyclerView, items: List<T>) {
    var itemPosition = 0

    for ((index, model) in items.withIndex()) {
        when (model) {
            is GolfRankModel -> if (model.isMe) itemPosition = index
            else -> throw IllegalArgumentException("None Type Error")
        }
    }

    rv.smoothScrollToPosition(itemPosition)
}