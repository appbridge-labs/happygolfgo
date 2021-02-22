package com.artilearn.happygolfgo.ui.home.mypage.profilechange

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityProfileChangeBinding
import com.artilearn.happygolfgo.ui.home.mypage.profilechange.ProfileChangeViewModel.ProfileChangeState.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileChangeActivity : BaseActivity<ActivityProfileChangeBinding>(
    R.layout.activity_profile_change
) {
    private val viewModel: ProfileChangeViewModel by viewModel()
    private val getContent = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { url ->
        setProfileImage(url.toString(), "변경")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initToolbar(binding.toolbar.toolbar, "프로필 변경", true)
        initViewModelObserving()
    }

    private fun initBinding() {
        binding.viewModel = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            state.observe(this@ProfileChangeActivity, Observer {
                when (it) {
                    GALLERY -> getContent.launch("image/*")
                    BACK -> finish()
                    DEFAULT_IMAGE -> setProfileImage(null, "취소")
                    UPDATE -> println("update")
                    SUCCESS -> ""
                    FAIL -> ""
                }
            })
        }
    }

    private fun setProfileImage(imagePath: String?, text: String) {
        if (imagePath == null) {
            viewModel.setProfileImage(null)
            binding.btnResult.text = text
        } else {
            viewModel.setProfileImage(imagePath)
            binding.btnResult.text = text
        }
    }

}