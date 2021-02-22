package com.artilearn.happygolfgo.ui.home.mypage.profilechange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityProfileChangeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileChangeActivity : BaseActivity<ActivityProfileChangeBinding>(
    R.layout.activity_profile_change
) {
    private val viewModel: ProfileChangeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initToolbar(binding.toolbar.toolbar, "프로필 변경", true)
    }

    private fun initBinding() {
        binding.viewModel = viewModel
    }
}