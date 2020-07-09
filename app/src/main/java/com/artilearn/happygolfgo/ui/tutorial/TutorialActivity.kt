package com.artilearn.happygolfgo.ui.tutorial

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityTutorialBinding
import com.artilearn.happygolfgo.ui.login.LoginActivity
import com.artilearn.happygolfgo.util.MyApplication

class TutorialActivity : BaseActivity<ActivityTutorialBinding>(R.layout.activity_tutorial) {

    private lateinit var myApplication: MyApplication
    private val viewModel: TutorialViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TutorialViewModel(
                    myApplication.tutorialRepository
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
            btnAnimation.observe(this@TutorialActivity, Observer {
                startAnimation()
            })
            goLogin.observe(this@TutorialActivity, Observer {
                goLogin()
            })
        }
    }

    private fun startAnimation() {
        binding.btnStart.apply {
            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .duration = 450L
        }
    }

    private fun goLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}