package com.artilearn.happygolfgo.ui.user

import android.os.Bundle
import android.view.View
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.adapter.user.AlramAdapter
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.models.Alram
import com.artilearn.happygolfgo.ui.BaseActivity
import com.artilearn.happygolfgo.util.SharedPreferencesHelper
import com.artilearn.happygolfgo.util.loading
import com.artilearn.happygolfgo.util.noticeDialog
import com.google.gson.internal.LinkedTreeMap
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult
import kotlinx.android.synthetic.main.activity_alram.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*

class AlramActivity : BaseActivity() {

    private lateinit var sp: SharedPreferencesHelper
    private lateinit var requestManager: RequestManager
    private lateinit var userManager: HappyGolfGoUserManager

    private lateinit var adapter: AlramAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alram)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.itemClear()
        if (this::requestManager.isInitialized) requestManager.rootActivity.finish()
    }

    private fun initView() {
        sp = SharedPreferencesHelper
        sp.init(this)

        requestManager = RequestManager(this)
        userManager = HappyGolfGoUserManager(this)
        adapter = AlramAdapter()

        setUpActionBar(toolbar_alram.toolbar, "알림", true)
        recyclerView.adapter = adapter

        request()
    }

    private fun request() {
        loading(progressBar, true)

        val request = HappyGolfGoRequest.pushAlram
        requestManager.request(request, userManager.user) { result, json ->
            when (result) {
                RequestResult.nSuccess -> {
                    runOnUiThread {
                        loading(progressBar, false)
                        noticeDialog(this, "알림", "요청 주소가 없습니다.")
                    }
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        200 -> {
                            val jsonItem =
                                json?.get("notifications") as? ArrayList<LinkedTreeMap<String, Any>>
                            val item = jsonItem?.mapNotNull { Alram.create(it) } as ArrayList<Alram>

                            if (item.size > 0) {
                                runOnUiThread {
                                    loading(progressBar, false)
                                    chlid_layout.visibility = View.GONE
                                    adapter.setItem(item)
                                }
                            } else {
                                runOnUiThread {
                                    loading(progressBar, false)
                                    chlid_layout.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
                RequestResult.failure -> {
                    runOnUiThread {
                        loading(progressBar, false)
                        noticeDialog(this, "알림", "인터넷 환경이 좋지 않습니다.")
                    }
                }
                else -> {
                    runOnUiThread {
                        loading(progressBar, false)
                        noticeDialog(this, "알림", "오류가 발생했습니다.")
                    }
                }
            }

        }
    }
}
