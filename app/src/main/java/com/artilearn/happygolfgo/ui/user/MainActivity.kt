package com.artilearn.happygolfgo.ui.user

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.adapter.TicketAdapter
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.models.Ticket
import com.artilearn.happygolfgo.ui.BaseActivity
import com.artilearn.happygolfgo.util.SharedPreferencesHelper
import com.artilearn.happygolfgo.util.noticeDialog
import com.google.gson.internal.LinkedTreeMap
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.RuntimeException


class MainActivity : BaseActivity(), TicketAdapter.TicketClick {

    private lateinit var sp: SharedPreferencesHelper

    private lateinit var ticketAdapter: TicketAdapter
    private lateinit var requestManager: RequestManager
    private lateinit var userManager: HappyGolfGoUserManager
    private lateinit var tk: Ticket

    private var noTicket = 0
    private var fcm = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        ticketAdapter.itemClear()
        if (this::requestManager.isInitialized) requestManager.rootActivity.finish()
    }

    override fun bottomButtonAction() {
        super.bottomButtonAction()

        if (noTicket != 0) {
            val intent = Intent(this, MyReservationListActivity::class.java)
            startActivity(intent)
        } else {
            noticeDialog(
                this,
                "이용권 없음",
                "보유하고 있는 이용권이 없습니다.\n먼저 이용권을 구매해 주세요."
            )
        }
    }

    override fun retryAction() {
        super.retryAction()
        fetchData()
    }

    override fun customActionBarRightAction() {
        super.customActionBarRightAction()

        val intent = Intent(this, MyPageActivity::class.java)
        startActivity(intent)
    }

    override fun actionAlram() {
        super.actionAlram()

        val intent = Intent(this, AlramActivity::class.java)
        startActivity(intent)
    }

    override fun ticket(ticket: Ticket) {
        tk = ticket
        when (ticket.type.integer) {
            2 -> {
                if (!ticket.allowToBook!!) {
                    noticeDialog(
                        root.context,
                        "예약제한",
                        "예약은 최대 3개 까지만 가능합니다."
                    )
                } else {
                    choiceDate()
                }
            }
            else -> {
                choiceDate()
            }
        }
    }

    private fun initView() {
        fcmMessage()

        sp = SharedPreferencesHelper
        sp.init(this)
        requestManager = RequestManager(this)
        userManager = HappyGolfGoUserManager(this)

        setFrameLayoutID(R.id.frame_layout_main)
        setBaseFragments()

        //setUpReachability()
        setDisplay()
        refreshLayout()
        setUpAdapter()
        fetchData()
    }

    private fun fcmMessage() {
        fcm = intent.getIntExtra("fcm", 9001)
        if (fcm != 9001) {
            startActivity(Intent(this, AlramActivity::class.java))
        }
    }

    private fun refreshLayout() {
        refresh.setOnRefreshListener {
            refresh.isRefreshing = false
            fetchData()
        }
    }

    private fun choiceDate() {
        val intent = Intent(this, SelectDateActivity::class.java)
        intent.putExtra("ticket", tk)
        startActivity(intent)
    }

    private fun setDisplay() {
        setUpCustomActionBar(
            custom_toolbar_main,
            getString(R.string.toolbar_title_main),
            getDrawable(R.drawable.ic_alram),
            getDrawable(R.drawable.ic_user_toolbar)
        )
        setUpBottomButton(bottom_button_main, getString(R.string.bottom_button_title_main))
    }

    private fun setUpAdapter() {
        ticketAdapter = TicketAdapter()
        ticketAdapter.listener = this
        recycler_view_ticket_main.adapter = ticketAdapter
    }

    private fun fetchData() {
        allHide()
        showLoading()

        val request = HappyGolfGoRequest.main
        requestManager.request(request, userManager.user) { result, json ->
            when (result) {
                RequestResult.nSuccess -> {
                    hideLoading()
                    return@request
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        200 -> {
                            val ticketsJSON =
                                json?.get("Tickets") as? ArrayList<LinkedTreeMap<String, Any>>
                            val item =
                                ticketsJSON?.mapNotNull { Ticket.create(it) } as? ArrayList<Ticket>

                            if (item?.size!! > 0) {
                                runOnUiThread {
                                    chlid_layout.visibility = View.GONE
                                    ticketAdapter.itemClear()
                                    noTicket = 1
                                    ticketAdapter.setItem(item)
                                }
                            } else {
                                runOnUiThread { chlid_layout.visibility = View.VISIBLE }
                            }
                            hideLoading()
                        }
                    }
                }
                RequestResult.failure -> {
                    runOnUiThread {
                        if (result.errorMessage === "timeout") {
                            chlid_layout.visibility = View.GONE
                            showNetworkDelay()
                        } else {
                            runOnUiThread {
                                chlid_layout.visibility = View.GONE
                                showUnknownError()
                            }
                        }
                        hideLoading()
                    }
                }
                RequestResult.tryAgain -> {
                    runOnUiThread {
                        hideLoading()
                        userManager.setNewToken(result.newToken)
                        fetchData()
                    }
                }
                else -> {
                    runOnUiThread {
                        chlid_layout.visibility = View.GONE
                        showUnknownError()
                        hideLoading()
                    }
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
                    chlid_layout.visibility = View.GONE
                    showNetworkFailure()
                } else {
                    hideNetworkFailure()
                }
            }
        }

}