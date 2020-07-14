package com.artilearn.happygolfgo.ui.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityMyPageBinding
import kotlinx.android.synthetic.main.activity_my_page.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class MyPageActivity : BaseActivity<ActivityMyPageBinding>(R.layout.activity_my_page) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(include_toolbar.toolBar, "Zzzzz", true)
    }
}