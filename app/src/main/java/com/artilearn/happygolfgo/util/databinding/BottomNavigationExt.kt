package com.artilearn.happygolfgo.util.databinding

import androidx.databinding.BindingAdapter
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.ui.home.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

@BindingAdapter("bottomClick")
fun BottomNavigationView.onBottomClick(viewModel: HomeViewModel) {
    setOnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.reservation -> viewModel.onNextBottomEvent("예약")
            //R.id.gamecenter -> { viewModel.test("게임센터") }
            R.id.alram -> viewModel.onNextBottomEvent("알림")
            R.id.mypage -> viewModel.onNextBottomEvent("마이페이지")
        }
        true
    }
}