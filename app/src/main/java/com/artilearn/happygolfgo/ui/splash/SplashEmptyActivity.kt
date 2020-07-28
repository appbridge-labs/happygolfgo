package com.artilearn.happygolfgo.ui.splash

import android.content.Intent
import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivitySplashEmptyBinding
import com.artilearn.happygolfgo.ui.alram.AlramActivity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class SplashEmptyActivity : BaseActivity<ActivitySplashEmptyBinding>(R.layout.activity_splash_empty) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rxDelay()
    }

    private fun rxDelay() {
        compositeDisposable.add(
            Completable.create { emitter ->
                emitter.onComplete()
            }
                .delay(1500L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    goAlram()
                }
        )
    }

    private fun goAlram() {
        startActivity(Intent(this, AlramActivity::class.java))
        finish()
    }
}