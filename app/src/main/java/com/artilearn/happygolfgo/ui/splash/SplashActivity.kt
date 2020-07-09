package com.artilearn.happygolfgo.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivitySplashBinding
import com.artilearn.happygolfgo.ui.login.LoginActivity
import com.artilearn.happygolfgo.ui.tutorial.TutorialActivity
import com.artilearn.happygolfgo.util.MyApplication

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private lateinit var myApplication: MyApplication
    private val viewModel: SplashViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SplashViewModel(
                    myApplication.splashRepository
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
            goTutorial.observe(this@SplashActivity, Observer {
                goTutorial()
            })
            goLogin.observe(this@SplashActivity, Observer {
                goLogin()
            })
            goMain.observe(this@SplashActivity, Observer {

            })
        }
    }

    private fun goLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun goTutorial() {
        startActivity(Intent(this, TutorialActivity::class.java))
        finish()
    }

    private fun goMain() {
        //startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}