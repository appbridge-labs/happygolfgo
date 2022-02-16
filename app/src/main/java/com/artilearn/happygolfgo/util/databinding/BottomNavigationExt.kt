package com.artilearn.happygolfgo.util.databinding

import androidx.databinding.BindingAdapter
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.ui.home.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tlsolution.tlsmodules.Network.RequestEnvironmentMode

@BindingAdapter("bottomClick")
fun BottomNavigationView.onBottomClick(viewModel: HomeViewModel) {
    setOnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.reservation -> viewModel.onNextBottomEvent("예약")
            //TODO: game center to rhythm coach 2022.01.09
//            R.id.gamecenter -> {
//                RequestEnvironmentMode.test("게임센터")
//            }
            //  version 39(2.2.3) 버그 수정 중
//          2022.02.15 리듬 코치 배포 보류 중
//            R.id.rhythmcoach -> viewModel.onNextBottomEvent("리듬코치");
            R.id.alram -> viewModel.onNextBottomEvent("알림")
            R.id.mypage -> viewModel.onNextBottomEvent("마이페이지")
        }
        true
    }
}