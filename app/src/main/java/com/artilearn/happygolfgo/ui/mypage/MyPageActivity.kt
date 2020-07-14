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
import com.artilearn.happygolfgo.constants.APP_STORE_PATH
import com.artilearn.happygolfgo.databinding.ActivityMyPageBinding
import com.artilearn.happygolfgo.ui.login.LoginActivity
import com.artilearn.happygolfgo.ui.myinfo.MyInfoActivity
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.PreferenceManager
import com.artilearn.happygolfgo.util.snackbar
import kotlinx.android.synthetic.main.activity_my_page.*
import kotlinx.android.synthetic.main.common_view.view.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class MyPageActivity : BaseActivity<ActivityMyPageBinding>(R.layout.activity_my_page) {
    private val TAG = javaClass.simpleName

    private var appVersion: String? = null
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
            showUpdate.observe(this@MyPageActivity, Observer {
                showUpdate()
            })
            goPlayStore.observe(this@MyPageActivity, Observer {
                updateAppStore()
            })
        }
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
            includeLogout.setOnClickListener { logout() }
        }

        appVersion = viewModel.loadAppVersion()
        appVersion?.let {
            binding.tvVersion.text = it
        }

    }

    private fun showUpdate() {
        binding.tvUpdate.visibility = View.VISIBLE
    }

    private fun updateAppStore() {
        Intent(Intent.ACTION_VIEW, Uri.parse(APP_STORE_PATH)).takeIf {
            it.resolveActivity(packageManager) != null
        }?.run(this::startActivity)
    }

    private fun logout() {
        PreferenceManager(this).userLogout()
        goLogin()
    }

    private fun goLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun goMyInfo() {
        startActivity(Intent(this, MyInfoActivity::class.java))
    }

}