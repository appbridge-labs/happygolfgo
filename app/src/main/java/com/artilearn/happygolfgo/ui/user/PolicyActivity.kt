package com.artilearn.happygolfgo.ui.user

import android.content.Intent
import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.ui.BaseActivity
import com.artilearn.happygolfgo.util.noticeDialog
import kotlinx.android.synthetic.main.activity_policy.*
import kotlinx.android.synthetic.main.partial_menu_with_right_arrow.view.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*

class PolicyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)

        initView()
    }

    private fun initView() {
        marketing.text_view_title.text = "마케팅 사용동의"
        use.text_view_title.text = "이용약관"
        infor.text_view_title.text = "개인정보 취급 방침"

        setUpActionBar(
            toolbar_policy.toolbar,
            "정책사항",
            true
        )

        onClickListener()
    }

    private fun onClickListener() {
        marketing.setOnClickListener { noticeDialog(this, "준비중", "준비중입니다...") }
        use.setOnClickListener { useActivity() }
        infor.setOnClickListener { noticeDialog(this, "준비중", "준비중입니다...") }
    }

    private fun useActivity() {
        val intent = Intent(this, PolicyUseActivity::class.java)
        startActivity(intent)
    }
}