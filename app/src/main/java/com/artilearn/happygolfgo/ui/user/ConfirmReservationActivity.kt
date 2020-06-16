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
import com.artilearn.happygolfgo.enummodel.ClassType
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.models.*
import com.artilearn.happygolfgo.ui.BaseActivity
import com.artilearn.happygolfgo.util.convertTime
import com.artilearn.happygolfgo.util.snackbar
import com.google.gson.internal.LinkedTreeMap
import com.tlsolution.tlsmodules.Extensions.showTwoActionableAlert
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult
import kotlinx.android.synthetic.main.activity_confirm_reservation.*
import kotlinx.android.synthetic.main.common_textview.view.*
import kotlinx.android.synthetic.main.partial_bottom_button.view.*
import kotlinx.android.synthetic.main.partial_text_section_view.view.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*

class ConfirmReservationActivity : BaseActivity() {

    private lateinit var requestManager: RequestManager
    private lateinit var userManager: HappyGolfGoUserManager
    private lateinit var request: HappyGolfGoRequest

    private lateinit var plateReservation: PlateReservation
    private lateinit var lessonReservation: LessonReservation
    private lateinit var plateCancel: PlateReservation
    private lateinit var lessonCancel: LessonReservation

    private lateinit var ticket: Ticket
    private lateinit var lessonTime: LessonTime

    private lateinit var type: Any
    private var statusType: Int = 0
    private var networkStatus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_reservation)

        requestManager = RequestManager(this)
        userManager = HappyGolfGoUserManager(this)

        setFrameLayoutID(R.id.frame_layout_confirm_reservation)
        setBaseFragments()
        setUpReachability()

        initView()
        setUpActionBar(
            custom_toolbar.toolbar,
            getString(R.string.toolbar_title_confirm_reservation),
            true
        )
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

    override fun bottomButtonAction() {
        super.bottomButtonAction()

        when (networkStatus) {
            9999 -> {
                rootlaytou.snackbar("인터넷 연결을 확인해 주세요")
            }
            else -> {
                dialog()
            }
        }
    }

    private fun dialog() {
        if (statusType != 0) {
            showTwoActionableAlert(
                getString(R.string.alert_title_cancel_reservation),
                getString(R.string.alert_message_cancel_reservation),
                getString(R.string.alert_choice_confrim),
                getString(R.string.alert_choice_cancel),
                { deleteReady() }, {}
            )
        } else {
            showTwoActionableAlert(
                getString(R.string.alert_title_confirm_reservation),
                getString(R.string.alert_message_confrim_reservation),
                getString(R.string.alert_choice_confrim),
                getString(R.string.alert_choice_cancel),
                { uploadReady() }, {}
            )
        }
    }

    private fun bottomCheck(type: ClassType.ModelType, isCancellable: Boolean) {
        when (type) {
            ClassType.ModelType.PLATE_CANCEL, ClassType.ModelType.LESSON_CANCEL -> {
                statusType = 1
                if (isCancellable) {
                    setUpBottomButton(
                        bottom_nav.layout_bottom_button,
                        getString(R.string.bottom_button_cancel_reservation)
                    )
                    bottom_nav.layout_bottom_button.setBackgroundColor(getColor(R.color.color_red))
                    bottom_nav.layout_bottom_button.image_view_flag_bottom_button.setImageResource(R.drawable.ic_cancel)
                } else {
                    bottom_nav.visibility = View.GONE
                }
            }
            ClassType.ModelType.PLATE_RESERVE, ClassType.ModelType.LESSON_RESERVE -> {
                setUpBottomButton(
                    bottom_nav.layout_bottom_button,
                    getString(R.string.bottom_button_title_confirm_reservation)
                )
                bottom_nav.layout_bottom_button.setBackgroundColor(getColor(R.color.color_green))
                bottom_nav.layout_bottom_button.image_view_flag_bottom_button.setImageResource(R.drawable.ic_flag_bottom_button)
            }
        }
    }


    private fun initView() {
        val infor = intent.getSerializableExtra("item")
        val tk = intent.getSerializableExtra("ticket")

        if (tk != null) ticket = tk as Ticket

        val start: List<String>
        val end: List<String>

        tv_title.text_view_title.text = "예약명"
        tv_date.text_view_title.text = "날짜"

        infor?.let {
            type = infor
            when (it) {
                is PlateReservation -> { // plate reserve cancel
                    plateCancel = it
                    bottomCheck(plateCancel.type, it.isCancellable!!)
                    start = convertTime(plateCancel.startDate).trim().split(":")
                    end = convertTime(plateCancel.endDate).trim().split(":")
                    tv_title.text_view_content.text = plateCancel.name
                    tv_date.text_view_content.text = "${start[0]}. ${start[1]}. ${start[2]}"
                    tv_time.text_view_title.text = "시간"
                    tv_time.text_view_content.text = "${start[3]}:${start[4]} ~ ${end[3]}:${end[4]}"
                    tv_policy_content.tv_text.text = getString(R.string.plate_reserve_policy)
                }
                is LessonReservation -> { // lesson reserve cancel
                    lessonCancel = it
                    bottomCheck(lessonCancel.type, it.isCancellable!!)
                    start = convertTime(lessonCancel.startDate).trim().split(":")
                    end = convertTime(lessonCancel.endDate).trim().split(":")
                    tv_title.text_view_content.text = lessonCancel.name
                    tv_date.text_view_content.text = "${start[0]}. ${start[1]}. ${start[2]}"
                    tv_time.text_view_title.text = "레슨 시간"
                    tv_time.text_view_content.text = "${start[3]}:${start[4]} ~ ${end[3]}:${end[4]}"
                    tv_policy_content.tv_text.text = getString(R.string.lesson_reserve_policy)
                }
                is PlateTime -> { // plate reserve
                    bottomCheck(it.type, true)
                    start = convertTime(it.startDate).trim().split(":")
                    end = convertTime(it.endDate).trim().split(":")
                    tv_title.text_view_content.text = ticket.name
                    tv_date.text_view_content.text = "${start[0]}. ${start[1]}. ${start[2]}"
                    tv_time.text_view_title.text = "시간"
                    tv_time.text_view_content.text = "${start[3]}:${start[4]} ~ ${end[3]}:${end[4]}"
                    tv_policy_content.tv_text.text = getString(R.string.plate_reserve_policy)
                }
                is LessonTime -> { // lesson reserve
                    lessonTime = it
                    bottomCheck(lessonTime.type, true)
                    start = convertTime(lessonTime.startDate).trim().split(":")
                    end = convertTime(lessonTime.endDate).trim().split(":")
                    tv_title.text_view_content.text = ticket.name
                    tv_date.text_view_content.text = "${start[0]}. ${start[1]}. ${start[2]}"
                    tv_time.text_view_title.text = "레슨 시간"
                    tv_time.text_view_content.text = "${start[3]}:${start[4]} ~ ${end[3]}:${end[4]}"
                    tv_policy_content.tv_text.text = getString(R.string.lesson_reserve_policy)
                }
            }
        }
    }

    private fun deleteReady() {
        when (type) {
            is PlateReservation -> {
                deleteData(plateCancel.id, plateCancel)
            }
            is LessonReservation -> {
                deleteData(lessonCancel.id, lessonCancel)
            }
        }
    }

    private fun deleteData(id: Int, type: Any) {
        when (type) {
            is PlateReservation -> {
                request = HappyGolfGoRequest.cancelPlateReservation
                request.endpoint = id.toString()
            }
            is LessonReservation -> {
                request = HappyGolfGoRequest.cancelLessonReservation
                request.endpoint = id.toString()
            }
        }

        deleteHttp(request)
    }

    private fun uploadReady() {
        when (type) {
            is PlateTime -> {
                val plate = type as PlateTime
                request = HappyGolfGoRequest.withOutPlateNumberReserve
                request.body =
                    "{\"Data\": {\"ticketID\": \"${ticket.id}\", \"plateAvailabilityID\": \"${plate.plateAvailabilityID}\" }}"
                plateHttp(request)
            }
            is LessonTime -> {
                request = HappyGolfGoRequest.makeLessonReservation
                request.body =
                    "{\"Data\": {\"ticketID\": \"${ticket.id}\", \"lessonAvailabilityID\": \"${lessonTime.lessonAvailabilityID}\" }}"
                lessonHttp(request)
            }
        }
    }

    private fun deleteHttp(body: HappyGolfGoRequest) {
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
                        201 -> {
                            val intent = Intent(this, CompletionActivity::class.java)
                            intent.putExtra("data", 0)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
                RequestResult.failure -> {
                    hideLoading()
                    if (result.errorMessage === "timeout") {
                        runOnUiThread { showNetworkDelay() }
                    } else {
                        runOnUiThread { showUnknownError() }
                    }
                }
                else -> {
                    hideLoading()
                    runOnUiThread { showUnknownError() }
                    return@request
                }
            }

        }
    }

    private fun plateHttp(body: HappyGolfGoRequest) {
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
                        201 -> {
                            val item = json?.get("plateReservation") as LinkedTreeMap<String, Any>
                            val data = PlateReservation.create(item)

                            data?.let { plateReservation = it }

                            val intent = Intent(this, CompletionActivity::class.java)
                            intent.putExtra("data", 1)
                            intent.putExtra("item", plateReservation)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
                RequestResult.failure -> {
                    hideLoading()
                    if (result.errorMessage === "timeout") {
                        runOnUiThread { showNetworkDelay() }
                    } else {
                        runOnUiThread { showUnknownError() }
                    }
                }
                else -> {
                    hideLoading()
                    runOnUiThread { showUnknownError() }
                    return@request
                }
            }

        }
    }

    private fun lessonHttp(body: HappyGolfGoRequest) {
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
                        201 -> {
                            val item = json?.get("lessonReservaion") as LinkedTreeMap<String, Any>
                            val data = LessonReservation.create(item)

                            data?.let { lessonReservation = it }

                            val intent = Intent(this, CompletionActivity::class.java)
                            intent.putExtra("data", 1)
                            intent.putExtra("item", lessonReservation)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
                RequestResult.failure -> {
                    hideLoading()
                    if (result.errorMessage === "timeout") {
                        runOnUiThread { showNetworkDelay() }
                    } else {
                        runOnUiThread { showUnknownError() }
                    }
                }
                else -> {
                    hideLoading()
                    runOnUiThread { showUnknownError() }
                    return@request
                }
            }

        }
    }

    private val mMessageReceiver: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                networkStatus = intent.getIntExtra("status", 0)
                allHide()

                if (networkStatus != 0) {
                    showNetworkFailure()
                } else {
                    hideNetworkFailure()
                }
            }
        }

}