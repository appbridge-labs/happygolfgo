package com.artilearn.happygolfgo.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.databinding.ActivityTutorialBinding
import com.artilearn.happygolfgo.util.SharedPreferencesHelper


class TutorialActivity : AppCompatActivity() {

    private var shortAnimationDuration: Int = 450
    private lateinit var binding: ActivityTutorialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_tutorial)
        SharedPreferencesHelper.init(this)
        Handler().postDelayed({ animButton() }, 1000)
    }

    private fun animButton() {
        binding.buttonStartTutorial.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(null)
        }

        initClickListener()
    }

    private fun initClickListener() {
        binding.buttonStartTutorial.setOnClickListener {
            SharedPreferencesHelper.setTutorial(1)
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

}
