package com.artilearn.happygolfgo.base

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.ui.common.ServerErrorFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.custom_bottom_view.view.*

abstract class BaseActivity<B: ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : AppCompatActivity() {
    private val TAG = javaClass.simpleName

    protected val compositeDisposable = CompositeDisposable()
    lateinit var binding: B

    protected var actionBar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
    }

    protected fun initToolbar(toolbar: View, title: String, backButton: Boolean) {
        actionBar = toolbar as Toolbar
        setSupportActionBar(actionBar)

        val actionBar = supportActionBar
        actionBar?.let {
            with(it) {
                setDisplayShowTitleEnabled(true)
                setTitle(title)
                setDisplayHomeAsUpEnabled(backButton)
            }
        }
    }

    protected fun initBottomView(bottomView: View, title: String, type: BottimViewType) {
        with(bottomView) {
            tv_text.text = title
            setOnClickListener { bottomAction() }
        }

        when (type) {
            BottimViewType.DEFAULT_TYPE -> {
                bottomView.iv_image.setImageResource(R.drawable.ic_flag_bottom_button)
                bottomView.bottomLayout.setBackgroundColor(getColor(R.color.color_green))
            }
            BottimViewType.CANCEL_TYPE -> {
                bottomView.iv_image.setImageResource(R.drawable.ic_cancel)
                bottomView.bottomLayout.setBackgroundColor(getColor(R.color.color_red))
            }
        }
    }

    protected fun showNetworkFail(frameLayoutId: Int, msg: String) {
        val serverErrorFragment = ServerErrorFragment.newInstance(msg)
        supportFragmentManager.beginTransaction()
            .replace(frameLayoutId, serverErrorFragment)
            .addToBackStack(null)
            .commit()
    }

    open fun bottomAction() {}

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}