package com.artilearn.happygolfgo.ui.home.mypage.profilechange

import android.os.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityProfileChangeBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.home.mypage.profilechange.ProfileChangeViewModel.ProfileChangeState.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.*

//todo 사진 업로드, 기본 업로드 완성되었지만 갤러리 갔다 취소? 하는 쪽 코드에서 에러뜸 uri.toString 널포인트 뜨는 듯
class ProfileChangeActivity : BaseActivity<ActivityProfileChangeBinding>(
    R.layout.activity_profile_change
) {
    private val TAG = javaClass.simpleName
    private val viewModel: ProfileChangeViewModel by viewModel()
    private val getContent = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { url ->
        setProfile(url.toString())
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
                    GALLERY -> {
                        getContent.launch("image/*")
                    }
                    BACK -> {
                        finish()
                    }
                    DEFAULT_IMAGE -> {
                        setProfile(CHANGE_DEFAULT_IMAGE)
                    }
                    SUCCESS -> {
                        showToast(ToastType.SUCCESS, "프로필 변경 완료")
                        finish()
                    }
                }
            })
            networkFail.observe(this@ProfileChangeActivity, Observer {
                showToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            serverError.observe(this@ProfileChangeActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            appRefresh.observe(this@ProfileChangeActivity, Observer {
                goRefresh()
            })
        }
    }

    private fun setProfile(uri: String?) {
        when {
            uri.isNullOrEmpty() -> {
                viewModel.onNextButtonText("취소")
            }
            uri == CHANGE_DEFAULT_IMAGE -> {
                with(viewModel) {
                    onNextButtonText("변경")
                    setProfileImage(null)
                    onNextUri("")
                }
            }
            else -> {
                with(viewModel) {
                    onNextButtonText("변경")
                    setProfileImage(uri)
                    onNextUri(uri.toString())
                }
            }
        }
    }

    companion object {
        private const val CHANGE_DEFAULT_IMAGE = "default_image"
    }

}