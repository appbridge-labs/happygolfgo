package com.artilearn.happygolfgo.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.artilearn.happygolfgo.ui.fragment.DataLoadingFragment
import com.artilearn.happygolfgo.ui.fragment.NetworkDelayFragment
import com.artilearn.happygolfgo.ui.fragment.NetworkFailureFragment
import com.artilearn.happygolfgo.ui.fragment.UnknownErrorFragment
import com.artilearn.happygolfgo.util.NetworkUtil
import com.tlsolution.tlsmodules.TLSActivity
import kotlinx.android.synthetic.main.partial_bottom_button.view.*
import kotlinx.android.synthetic.main.partial_custom_toolbar.view.*


open class BaseActivity : TLSActivity(), LifecycleObserver {

    private var actionBar: androidx.appcompat.widget.Toolbar? = null
    private var customActionBar: View? = null
    private var networkStatusReceiver: BroadcastReceiver? = null

    private var frameLayoutViewID: Int? = null

    private val dataLoadingFragment = DataLoadingFragment()
    private val unknownErrorFragment = UnknownErrorFragment()

    private lateinit var networkFailureFragment: NetworkFailureFragment
    private lateinit var networkDelayFragment: NetworkDelayFragment
    private lateinit var fragmentManager: FragmentManager

    private var userStatus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentManager = supportFragmentManager
        networkFailureFragment = NetworkFailureFragment(this)
        networkDelayFragment = NetworkDelayFragment(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded(): Int {
        userStatus = 1
        return userStatus
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onAppPaused(): Int {
        userStatus = 2
        return userStatus
    }

    override fun onDestroy() {
        super.onDestroy()

        val check = networkStatusReceiver

        if (check != null) unregisterReceiver(networkStatusReceiver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showInternetError() {
        val intent = Intent("network-status")
        intent.putExtra("status", 9999)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    fun hideInternetError() {
        val intent = Intent("network-status")
        intent.putExtra("status", 0)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    fun setUpReachability() {
        networkStatusReceiver = (object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val status = NetworkUtil.getConnectivityStatus(this@BaseActivity)

                if (status == 0 && userStatus == 1) {
                    showInternetError()
                } else if (status != 0 && userStatus == 1) {
                    hideInternetError()
                }
            }
        })
        registerReceiver(
            networkStatusReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    // 커스텀 액션바
    fun setUpCustomActionBar(
        customToolbar: View,
        title: String? = null,
        alramImageButton: Drawable? = null,
        rightButtonImage: Drawable? = null
    ) {

        this.customActionBar = customToolbar

        customToolbar.text_view_title_custom_toolbar.text = title

        customToolbar.ib_alram.visibility = if (alramImageButton != null) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }

        customToolbar.rightImageButton.visibility = if (rightButtonImage != null) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }

        customToolbar.ib_alram.setImageDrawable(alramImageButton)
        customToolbar.rightImageButton.setImageDrawable(rightButtonImage)

        customToolbar.ib_alram.setOnClickListener { actionAlram() }
        customToolbar.rightImageButton.setOnClickListener { customActionBarRightAction() }
    }

    // 디폴트 액션바
    fun setUpActionBar(toolbarView: View, title: String, showBackButton: Boolean) {
        actionBar = toolbarView as androidx.appcompat.widget.Toolbar
        setSupportActionBar(actionBar)
        val actionBar = supportActionBar
        actionBar?.let {
            with(it) {
                setDisplayShowTitleEnabled(true)
                setTitle(title)
                setDisplayHomeAsUpEnabled(showBackButton)
            }
        }
    }

    //    bottomButtonAction: () -> Unit? = { }
    // 바텀 버튼
    fun setUpBottomButton(
        bottomButton: View,
        title: String?
    ) {

        with(bottomButton) {
            if (title != null) text_view_title_bottom_button.text = title

            setOnClickListener { bottomButtonAction() }
        }

    }

    // 커스텀된 액션바의 오른쪽 버튼을 눌렀을때의 이벤트 액션
    open fun customActionBarRightAction() {}

    open fun actionAlram() {}

    // 하단의 공통 버튼을 눌렀을 때의 이벤트의 액션
    open fun bottomButtonAction() {}

    // 네트워크 연결 실패시에 '다시시도'할 때 필요한 액션을 넣는다.
    open fun retryAction() {}

    fun setFrameLayoutID(viewID: Int) {
        frameLayoutViewID = viewID
    }

    fun setBaseFragments() {
        fragmentManager.beginTransaction().add(frameLayoutViewID!!, networkDelayFragment)
            .hide(networkDelayFragment).commit()
        fragmentManager.beginTransaction().add(frameLayoutViewID!!, networkFailureFragment)
            .hide(networkFailureFragment).commit()
        fragmentManager.beginTransaction().add(frameLayoutViewID!!, dataLoadingFragment)
            .hide(dataLoadingFragment).commit()
        fragmentManager.beginTransaction().add(frameLayoutViewID!!, unknownErrorFragment)
            .hide(unknownErrorFragment).commit()
    }

    fun showNetworkFailure() {
        fragmentManager
            .beginTransaction()
            .show(networkFailureFragment)
            .commit()
    }

    fun hideNetworkFailure(retryAction: () -> Unit? = {}) {
        fragmentManager
            .beginTransaction()
            .hide(networkFailureFragment)
            .commit()
        retryAction()
    }

    fun showLoading() {
        fragmentManager
            .beginTransaction()
            .show(dataLoadingFragment)
            .commit()
    }

    fun hideLoading() {
        fragmentManager
            .beginTransaction()
            .hide(dataLoadingFragment)
            .commitAllowingStateLoss()
    }

    fun showUnknownError() {
        fragmentManager
            .beginTransaction()
            .show(unknownErrorFragment)
            .commit()
    }

    fun showNetworkDelay() {
        fragmentManager
            .beginTransaction()
            .show(networkDelayFragment)
            .commit()
    }

    fun hideNetworkDelay(retryAction: () -> Unit? = {}) {
        fragmentManager
            .beginTransaction()
            .hide(networkDelayFragment)
            .commit()
        retryAction()
    }

    fun allHide() {
        fragmentManager.beginTransaction().hide(networkDelayFragment).commit()
        fragmentManager.beginTransaction().hide(networkFailureFragment).commit()
        fragmentManager.beginTransaction().hide(dataLoadingFragment).commit()
        fragmentManager.beginTransaction().hide(unknownErrorFragment).commit()
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}