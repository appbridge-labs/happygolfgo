package com.artilearn.happygolfgo.ui.user

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.adapter.user.MyReservationListAdapter
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.ui.BaseActivity
import com.artilearn.happygolfgo.util.snackbar
import com.google.gson.internal.LinkedTreeMap
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult
import kotlinx.android.synthetic.main.activity_my_reservation_list.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*


class MyReservationListActivity : BaseActivity() {

    private lateinit var requestManager: RequestManager
    private lateinit var userManager: HappyGolfGoUserManager
    private lateinit var fragmentData: LinkedTreeMap<String, Any>
    private lateinit var viewPagerAdapter: MyReservationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_reservation_list)

        initView()
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mMessageReceiver, IntentFilter("network-status"))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::requestManager.isInitialized) requestManager.rootActivity.finish()
    }

    private fun initView() {
        requestManager = RequestManager(this)
        userManager = HappyGolfGoUserManager(this)

        setFrameLayoutID(R.id.frameLayout)
        setBaseFragments()

        setUpReachability()
        setDisplay()
        fetchData()
    }

    private fun setDisplay() {
        runOnUiThread {
            setUpActionBar(
                toolbar_my_reservation_list.toolbar,
                getString(R.string.toolbar_title_my_reservation_list),
                true
            )
        }

        toolbar_my_reservation_list.actionBarSeparator.visibility = View.INVISIBLE
        toolbar_my_reservation_list.elevation.div(0)
    }

    private fun isLoading(loading: Boolean) {
        progressBar.visibility = if (loading) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun fetchData() {
        isLoading(true)

        val request = HappyGolfGoRequest.reservationList
        requestManager.request(request, userManager.user) { result, json ->

            when (result) {
                RequestResult.nSuccess -> {
                    runOnUiThread { isLoading(false) }
                }
                RequestResult.pSuccess -> {
                    runOnUiThread { isLoading(false) }
                    check(json != null) { return@request }
                    when (result.statusCode) {
                        200 -> {
                            fragmentData = json

                            viewPagerAdapter = MyReservationListAdapter(
                                supportFragmentManager,
                                fragmentData
                            )

                            runOnUiThread {
                                view_pager_my_reservation_list.adapter = viewPagerAdapter
                                tab_my_reservation_list.setupWithViewPager(
                                    view_pager_my_reservation_list
                                )
                            }

                        }
                    }
                }
                RequestResult.failure -> { // 실패의 경우
                    runOnUiThread { isLoading(false) }
                    root.snackbar("인터넷 연결 확인이 필요합니다")

                }
                else -> {
                    runOnUiThread { isLoading(false) }
                    root.snackbar("서버 상태가 좋지 않습니다")
                    return@request
                }
            }

        }
    }

    private val mMessageReceiver: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val num = intent.getIntExtra("status", 0)
                allHide()

                if (num != 0) {
                    showNetworkFailure()
                } else {
                    hideNetworkFailure()
                }
            }
        }

}


