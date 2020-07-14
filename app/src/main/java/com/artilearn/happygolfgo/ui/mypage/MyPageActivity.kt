package com.artilearn.happygolfgo.ui.mypage

import android.os.Bundle
import android.util.Log
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityMyPageBinding
import com.artilearn.happygolfgo.util.snackbar
import kotlinx.android.synthetic.main.activity_my_page.*
import kotlinx.android.synthetic.main.common_view.view.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class MyPageActivity : BaseActivity<ActivityMyPageBinding>(R.layout.activity_my_page) {
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        initToolbar(include_toolbar.toolBar, getString(R.string.mypage_toolbar_title), true)

        with(binding) {
            includeLesson.tv_text.text = getString(R.string.mypage_lesson_title)
            includeInfo.tv_text.text = getString(R.string.mypage_info_change)
            includePassword.tv_text.text = getString(R.string.mypage_password_change)
            includeLogout.tv_text.text = getString(R.string.mypage_logout)

            includeLesson.setOnClickListener { it.snackbar("includeLesson") }
            includeInfo.setOnClickListener { it.snackbar("includeInfo") }
            includePassword.setOnClickListener { it.snackbar("includePassword") }
            includeLogout.setOnClickListener { it.snackbar("includeLogout") }
        }

    }


}