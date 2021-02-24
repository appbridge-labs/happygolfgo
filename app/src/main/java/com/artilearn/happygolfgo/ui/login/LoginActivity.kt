package com.artilearn.happygolfgo.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.base.BaseLoadingFragment
import com.artilearn.happygolfgo.constants.LOADING
import com.artilearn.happygolfgo.databinding.ActivityLoginBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.common.DeveloperOptionFragment
import com.artilearn.happygolfgo.ui.home.HomeActivity
import com.artilearn.happygolfgo.ui.pwauth.PwAuthActivity
import com.artilearn.happygolfgo.ui.pwreset.PwResetActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val viewModel: LoginViewModel by viewModel()
    private val loginBottomView by lazy(::LoginBottomFragment)
    private val devOptionView by lazy(::DeveloperOptionFragment)
    private val loadingView by lazy { BaseLoadingFragment(R.layout.fragment_loading) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initTextListener()
    }

    override fun onStart() {
        super.onStart()

        //todo 버그리스트 19번 여러지점 등록 시 클라이언트가 지점 선택
        val bundle = Bundle()
        val test = ArrayList<String>()
        test.add("삼성점")
        test.add("의왕점")
        test.add("제주점")
        bundle.putStringArrayList("test", test)
        loginBottomView.arguments = bundle
        loginBottomView.show(supportFragmentManager, loginBottomView.tag)
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            networkFail.observe(this@LoginActivity, Observer {
                showToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            serverError.observe(this@LoginActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            forgetPassword.observe(this@LoginActivity, Observer {
                goPasswordAuth()
            })
            developerOption.observe(this@LoginActivity, Observer {
                devOptionView.show(supportFragmentManager, devOptionView.tag)
            })
            loginType.observe(this@LoginActivity, Observer {
                when (loginType.value) {
                    LoginViewModel.LoginType.EMPTY_PHONE -> showEmptyPhone()
                    LoginViewModel.LoginType.EMPTY_PASSWORD -> showEmptyPassword()
                    LoginViewModel.LoginType.NOT_FOUND_USER -> showEmptyUser()
                    LoginViewModel.LoginType.FAIL_PASSWORD -> showFailPassword()
                    LoginViewModel.LoginType.SUCCESS -> goHome()
                    LoginViewModel.LoginType.SHOW_LOADING -> loadingView.show(supportFragmentManager, LOADING)
                    LoginViewModel.LoginType.HIDE_LOADING -> loadingView.dismissAllowingStateLoss()
                }
            })
            appRefresh.observe(this@LoginActivity, Observer {
                goRefresh()
            })
        }
    }

    private fun initTextListener() {
        with(binding) {
            etPhone.addTextChangedListener { viewModel.phoneOnNext(it.toString()) }
            etPw.addTextChangedListener { viewModel.passwordOnNext(it.toString()) }
        }
    }

    private fun showEmptyUser() {
        binding.etPhone.error = getString(R.string.login_empty_user)
    }

    private fun showEmptyPhone() {
        binding.etPhone.error = getString(R.string.login_empth_phone)
    }

    private fun showEmptyPassword() {
        binding.etPw.error = getString(R.string.login_empty_password)
    }

    private fun showFailPassword() {
        binding.etPw.error = getString(R.string.login_fail_password)
    }

    private fun goHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun goPasswordAuth() {
        startActivity(Intent(this, PwAuthActivity::class.java))
    }

}