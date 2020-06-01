package com.artilearn.happygolfgo.ui.user

import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_policy__use.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*

class PolicyUseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy__use)

        initView()
    }

    private fun initView() {
        setUpActionBar(toolbar_policy_use.toolbar, "이용약관", true)
    }

}
