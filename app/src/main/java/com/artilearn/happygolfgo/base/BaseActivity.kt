package com.artilearn.happygolfgo.base

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.CommonFragmentType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.calendar.CalendarFragment
import com.artilearn.happygolfgo.ui.common.NetworkDelayFragment
import com.artilearn.happygolfgo.ui.common.NetworkFailFragment
import com.artilearn.happygolfgo.ui.common.ServerErrorFragment
import com.artilearn.happygolfgo.ui.home.HomeFragment
import es.dmoral.toasty.Toasty
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.custom_bottom_view.view.*

abstract class BaseActivity<B: ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : AppCompatActivity() {
    private val TAG = javaClass.simpleName

    protected val compositeDisposable = CompositeDisposable()
    lateinit var binding: B

    private var actionBar: Toolbar? = null

    private lateinit var networkDelayFragment: NetworkDelayFragment
    private lateinit var networkFailFragment: NetworkFailFragment
    private lateinit var serverErrorFragment: ServerErrorFragment
    private lateinit var homeFragment: HomeFragment
    private lateinit var calendarFragment: CalendarFragment

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

    protected fun showCommonFragment(
        frameLayoutId: Int,
        message: String?,
        type: CommonFragmentType,
        item: Any? = null
    ) {
        when (type) {
            CommonFragmentType.NETWORK_FAIL -> {
                networkFailFragment = NetworkFailFragment.newInstance()
                initFragment(frameLayoutId, networkFailFragment)
            }
            CommonFragmentType.NETWORK_DELAY -> {

            }
            CommonFragmentType.SERVER_ERROR -> {
                serverErrorFragment = ServerErrorFragment.newInstance(message)
                initFragment(frameLayoutId, serverErrorFragment)
            }
            CommonFragmentType.HOME -> {
                homeFragment = HomeFragment.newInstance()
                initFragment(frameLayoutId, homeFragment)
            }
            CommonFragmentType.CALENDAR -> {
                val ticket = item as Ticket
                calendarFragment = CalendarFragment.newInstance(ticket)
                initFragment(frameLayoutId, calendarFragment)
            }
        }
    }

    protected fun showToast(type: ToastType, message: String) {
        when (type) {
            ToastType.SUCCESS -> Toasty.success(this, message, Toast.LENGTH_SHORT).show()
            ToastType.ERROR -> Toasty.error(this, message, Toast.LENGTH_SHORT).show()
            ToastType.WARNNING -> Toasty.warning(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initFragment(frameLayoutId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(frameLayoutId, fragment)
            .commit()
    }

    open fun bottomAction() {}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}