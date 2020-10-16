package com.artilearn.happygolfgo.ui.mypage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.PLAY_STORE
import com.artilearn.happygolfgo.databinding.ActivityMyPageBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.lessonpostlist.LessonPostListActivity
import com.artilearn.happygolfgo.ui.login.LoginActivity
import com.artilearn.happygolfgo.ui.myinfo.MyInfoActivity
import com.artilearn.happygolfgo.ui.pwauth.PwAuthActivity
import com.artilearn.happygolfgo.util.MyApplication
import kotlinx.android.synthetic.main.common_view.view.*

class MyPageActivity : BaseActivity<ActivityMyPageBinding>(R.layout.activity_my_page) {

    private lateinit var myApplication: MyApplication

    private val viewModel: MyPageViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MyPageViewModel(
                    myApplication.versionRepository
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
        initViewModelObserve()
        initView()
    }

    private fun inject() {
        myApplication = application as MyApplication
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            logout.observe(this@MyPageActivity, Observer {
                goLogin()
            })
            fail.observe(this@MyPageActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            versionText.observe(this@MyPageActivity, Observer { version ->
                binding.tvVersion.text = version
            })
            update.observe(this@MyPageActivity, Observer {
                when (update.value) {
                    MyPageViewModel.UpdateModel.NEED -> showUpdate()
                    MyPageViewModel.UpdateModel.NECESSARY -> hideUpdate()
                }
            })
            playStore.observe(this@MyPageActivity, Observer {
                goPlayStore()
            })
        }
    }

    private fun initView() {
        initToolbar(binding.mypageToolbar.toolbar, getString(R.string.mypage_toolbar_title), true)

        with(binding) {
            includeLesson.tv_text.text = getString(R.string.mypage_lesson_title)
            includeInfo.tv_text.text = getString(R.string.mypage_info_change)
            includePassword.tv_text.text = getString(R.string.mypage_password_change)
            includeLogout.tv_text.text = getString(R.string.mypage_logout)

            includeLesson.setOnClickListener { goLessonLog() }
            includeInfo.setOnClickListener { goMyInfo() }
            includePassword.setOnClickListener { goPwAuth() }
            includeLogout.setOnClickListener { logout() }
        }
    }

    private fun logout() {
        viewModel.userLogout()
    }

    private fun goLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun goMyInfo() {
        startActivity(Intent(this, MyInfoActivity::class.java))
    }

    private fun goPwAuth() {
        startActivity(Intent(this, PwAuthActivity::class.java))
    }

    private fun goLessonLog() {
        startActivity(Intent(this, LessonPostListActivity::class.java))
    }

    private fun showUpdate() {
        binding.tvUpdate.visibility = View.VISIBLE
    }

    private fun hideUpdate() {
        binding.tvUpdate.visibility = View.GONE
    }

    private fun goPlayStore() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(PLAY_STORE)
            setPackage("com.android.vending")
        }
        startActivity(intent)
    }

}