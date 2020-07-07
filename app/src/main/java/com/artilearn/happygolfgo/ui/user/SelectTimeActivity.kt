package com.artilearn.happygolfgo.ui.user

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.adapter.LessonTimeAdapter
import com.artilearn.happygolfgo.adapter.PlateTimeAdapter
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.models.LessonTime
import com.artilearn.happygolfgo.models.PlateTime
import com.artilearn.happygolfgo.models.Ticket
import com.artilearn.happygolfgo.ui.BaseActivity
import com.artilearn.happygolfgo.util.snackbar
import com.google.gson.internal.LinkedTreeMap
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult
import kotlinx.android.synthetic.main.activity_select_time.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*

class SelectTimeActivity : BaseActivity() {

    private lateinit var ticket: Ticket
    private lateinit var request: HappyGolfGoRequest
    private lateinit var requestManager: RequestManager
    private lateinit var userManager: HappyGolfGoUserManager
    private lateinit var plateTimeAdapter: PlateTimeAdapter
    private lateinit var lessonTimeAdapter: LessonTimeAdapter

    private var date: String? = null
    private var data: List<String>? = null
    private var check = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_time)
        requestManager = RequestManager(this)
        userManager = HappyGolfGoUserManager(this)

        setFrameLayoutID(R.id.frame_layout_select_time)
        setBaseFragments()

        //setUpReachability()
        initData()
        initAdapter()
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
        if (check == 1) plateTimeAdapter.isPlateClear()
        else lessonTimeAdapter.isLessonClear()

        if (this::requestManager.isInitialized) requestManager.rootActivity.finish()
    }

    private fun initData() {
        ticket = intent.getSerializableExtra("ticket") as Ticket
        date = intent.getStringExtra("idx")

        if (date != null) {
            data = date!!.trim().split("::")
            fetchData()
        }

        when (ticket.type.integer) {
            0 -> {
                setUpActionBar(
                    toolbar_select_time.toolbar,
                    getString(R.string.lessontime),
                    true
                )
            }
            2 -> {
                setUpActionBar(
                    toolbar_select_time.toolbar,
                    "타석 시간 선택",
                    true
                )
            }
        }

    }

    private fun initAdapter() {
        plateTimeAdapter = PlateTimeAdapter()
        lessonTimeAdapter = LessonTimeAdapter()
        initListener()
    }

    private fun timeCheck(v: Boolean) {
        child.visibility = if (v) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun initListener() {
        lessonTimeAdapter.setItemClickListener { lesson ->
            if (lesson.status == 0) {
                root.snackbar("예약이 완료된 시간입니다")
            } else {
                val intent = Intent(this, ConfirmReservationActivity::class.java)
                intent.putExtra("item", lesson)
                intent.putExtra("ticket", ticket)
                intent.putExtra("idx", date)
                startActivity(intent)
            }
        }

        plateTimeAdapter.plateOnClick { plate ->
            if (plate.availableCount == 0) {
                root.snackbar("잔여 타석이 없습니다")
            } else {
                val intent = Intent(this, ConfirmReservationActivity::class.java)
                intent.putExtra("item", plate)
                intent.putExtra("ticket", ticket)
                intent.putExtra("idx", date)
                startActivity(intent)
            }
        }
    }

    private fun fetchData() {
        when (ticket.type.integer) {
            0 -> {
                request = HappyGolfGoRequest.selectLessonTime
            }
            2 -> {
                request = HappyGolfGoRequest.selectPlateTime
            }
        }

        date?.let {
            request.endpoint = it
            http(request)
        }
    }

    private fun http(body: HappyGolfGoRequest) {
        runOnUiThread { timeCheck(false) }
        allHide()
        showLoading()

        requestManager.request(body, userManager.user) { result, json ->

            when (result) {
                RequestResult.nSuccess -> {
                    hideLoading()
                    return@request
                }
                RequestResult.pSuccess -> {
                    hideLoading()
                    when (result.statusCode) {
                        200 -> {
                            val item =
                                json?.get("availableTimes") as ArrayList<LinkedTreeMap<String, Any>>

                            if (item.size > 0) {
                                runOnUiThread { timeCheck(false) }
                                when {
                                    item[0].containsKey("plateAvailabilityID") -> {
                                        check = 1
                                        val plateData =
                                            item.map { PlateTime.create(it) } as ArrayList<PlateTime>

                                        runOnUiThread {
                                            recyclerView.adapter = plateTimeAdapter
                                            plateTimeAdapter.setPlate(plateData)
                                        }
                                    }
                                    item[0].containsKey("lessonAvailabilityID") -> {
                                        val lessonData =
                                            item.map { LessonTime.create(it) } as ArrayList<LessonTime>

                                        runOnUiThread {
                                            recyclerView.adapter = lessonTimeAdapter
                                            lessonTimeAdapter.setLesson(lessonData)
                                        }
                                    }
                                    else -> {
                                        runOnUiThread { timeCheck(true) }
                                    }
                                }
                            } else {
                                runOnUiThread { timeCheck(true) }
                            }
                        }
                    }
                }
                RequestResult.failure -> {
                    hideLoading()
                    if (result.errorMessage === "timeout") {
                        showNetworkDelay()
                    }

                    val failureMessage: String

                    when (result.statusCode) {
                        else -> failureMessage = result.errorMessage ?: "unknown error"
                    }
                    root.snackbar(failureMessage)
                }
                else -> {
                    hideLoading()
                    runOnUiThread { showUnknownError() }
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
