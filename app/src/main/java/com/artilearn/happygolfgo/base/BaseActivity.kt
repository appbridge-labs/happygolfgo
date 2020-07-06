package com.artilearn.happygolfgo.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity<B: ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : AppCompatActivity() {

    protected val compositeDisposable = CompositeDisposable()
    lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}