package com.artilearn.happygolfgo.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityLoginBinding
import com.artilearn.happygolfgo.ui.home.HomeActivity
import com.artilearn.happygolfgo.util.MyApplication

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private lateinit var myApplication: MyApplication
    private val viewModel: LoginViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return LoginViewModel(
                    myApplication.loginRepository
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
        initViewModelObserve()
    }

    private fun inject() {
        myApplication = application as MyApplication
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            networkFail.observe(this@LoginActivity, Observer {

            })
            networkDelay.observe(this@LoginActivity, Observer {

            })
            serverError.observe(this@LoginActivity, Observer {

            })
            emptyPhone.observe(this@LoginActivity, Observer {
                showEmptyPhone()
            })
            emptyPassword.observe(this@LoginActivity, Observer {
                showEmptyPassword()
            })
            emptyUser.observe(this@LoginActivity, Observer {
                showEmptyUser()
            })
            failPassword.observe(this@LoginActivity, Observer {
                showFailPassword()
            })
            loginSuccess.observe(this@LoginActivity, Observer {
                goHome()
            })
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
}