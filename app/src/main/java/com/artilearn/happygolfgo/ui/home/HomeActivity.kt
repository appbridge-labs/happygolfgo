package com.artilearn.happygolfgo.ui.home

import android.os.Bundle
import android.util.Log
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityHomeBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun inject() {

    }

    private fun initViewModelObserve() {

    }

    private fun initView() {
        initBottomView(binding.includeBottomView, getString(R.string.home_bottomview_title), BottimViewType.DEFAULT_TYPE)
    }

    override fun bottomAction() {
        super.bottomAction()
        Log.d(TAG, "bottomAction: ")
    }


}