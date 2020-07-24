package com.artilearn.happygolfgo.ui.common

import android.content.Intent
import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.EXTRA_ERROR_TEXT
import com.artilearn.happygolfgo.constants.EXTRA_INTENT
import com.artilearn.happygolfgo.databinding.ActivityErrorBinding

class ErrorActivity : BaseActivity<ActivityErrorBinding>(R.layout.activity_error) {

    private val lastActivityIntent by lazy { intent.getParcelableExtra<Intent>(EXTRA_INTENT) }
    private val errorText by lazy { intent.getStringExtra(EXTRA_ERROR_TEXT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showEvent()
        initClickListener()
    }

    private fun showEvent() {
        with(binding) {
            tvErrorTitle.apply {
                animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .startDelay = 1500L
            }
            tvErrorMessage.apply {
                this.text = errorText
                animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .startDelay = 1500L
            }
            btmReload.apply {
                animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .startDelay = 1500L
            }
        }
    }

    private fun initClickListener() {
        binding.btmReload.setOnClickListener {
            startActivity(lastActivityIntent)
            finish()
        }
    }

}