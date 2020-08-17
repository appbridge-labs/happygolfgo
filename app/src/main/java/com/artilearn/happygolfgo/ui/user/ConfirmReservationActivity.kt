package com.artilearn.happygolfgo.ui.user

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.enummodel.ClassType
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.models.*
import com.artilearn.happygolfgo.util.convertTime
import com.artilearn.happygolfgo.util.noticeDialog
import com.google.gson.internal.LinkedTreeMap
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult
import kotlinx.android.synthetic.main.partial_bottom_button.view.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*

class ConfirmReservationActivity : BaseActivity() {

    private lateinit var requestManager: RequestManager
    private lateinit var userManager: HappyGolfGoUserManager
    private lateinit var request: HappyGolfGoRequest

    private lateinit var plateReservation: PlateReservation
    private lateinit var lessonReservation: LessonReservation
    private var plateCancel: PlateReservation? = null
    private var lessonCancel: LessonReservation? = null
    private var policy: Policy? = null

    private lateinit var ticket: Ticket

    private lateinit var type: Any
    private lateinit var cancelType: Any
    private var statusType: Int = 0
    private var networkStatus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_reservation)

        requestManager = RequestManager(this)
        userManager = HappyGolfGoUserManager(this)

        setFrameLayoutID(R.id.frame_layout_confirm_reservation)
        setBaseFragments()
        //setUpReachability()

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
        val reserve = intent.getSerializableExtra("reserve")

        if (tk != null) ticket = tk as Ticket

        infor?.let {
            type = infor
            when (it) {
                is PlateTime -> {
                    platePolicyData()
                }
                is LessonTime -> {
                    bottomCheck(it.type, true)
                    val start: List<String> = convertTime(it.startDate).trim().split(":")
                    val end: List<String> = convertTime(it.endDate).trim().split(":")
                    tv_lessonName.text = ticket.name
                    tv_date.text = "${start[0]}. ${start[1]}. ${start[2]}"
                    tv_time.text = "${start[3]}:${start[4]} ~ ${end[3]}:${end[4]}"
                    tv_policy.text = getString(R.string.lesson_reserve_policy)
                }
            }
        }

        reserve?.let {
            type = it
            cancelType = it
            requestData()
        }
    }

    private fun cancelInitData() {
        val start: List<String>
        val end: List<String>

        when (type) {
            is PlateReservation -> {
                bottomCheck(plateCancel!!.type, plateCancel?.isCancellable!!)
                start = convertTime(plateCancel!!.startDate).trim().split(":")
                end = convertTime(plateCancel!!.endDate).trim().split(":")
                tv_lessonName.text = plateCancel!!.name
                tv_date.text = "${start[0]}. ${start[1]}. ${start[2]}"
                tv_time.text = "${start[3]}:${start[4]} ~ ${end[3]}:${end[4]}"
                tv_policy.text = policy?.body
            }
            is LessonReservation -> {
                bottomCheck(lessonCancel!!.type, lessonCancel!!.isCancellable!!)
                start = convertTime(lessonCancel!!.startDate).trim().split(":")
                end = convertTime(lessonCancel!!.endDate).trim().split(":")
                tv_lessonName.text = lessonCancel!!.name
                tv_date.text = "${start[0]}. ${start[1]}. ${start[2]}"
                tv_time.text = "${start[3]}:${start[4]} ~ ${end[3]}:${end[4]}"
                tv_policy.text = getString(R.string.lesson_reserve_policy)
            }
        }
    }

    private fun reservePlatePolicy() {
        val plateTime = type as PlateTime

        bottomCheck(plateTime.type, true)
        val start: List<String> = convertTime(plateTime.startDate).trim().split(":")
        val end: List<String> = convertTime(plateTime.endDate).trim().split(":")
        tv_lessonName.text = ticket.name
        tv_date.text = "${start[0]}. ${start[1]}. ${start[2]}"
        tv_time.text = "${start[3]}:${start[4]} ~ ${end[3]}:${end[4]}"
        tv_policy.text = policy?.body
    }

    private fun deleteReady() {
        when (type) {
            is PlateReservation -> {
                deleteData(plateCancel!!.id, plateCancel)
            }
            is LessonReservation -> {
                deleteData(lessonCancel!!.id, lessonCancel)
            }
        }
    }

    private fun requestData() {
        when (cancelType) {
            is PlateReservation -> {
                request = HappyGolfGoRequest.plateDetail
                request.endpoint = (cancelType as PlateReservation).id.toString()
                reservePlate(request)
            }
            is LessonReservation -> {
                request = HappyGolfGoRequest.lessonDetail
                request.endpoint = (cancelType as LessonReservation).id.toString()
                reserveLesson(request)
            }
        }
    }

    private fun deleteData(id: Int, type: Any?) {
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
                val lesson = type as LessonTime
                request = HappyGolfGoRequest.makeLessonReservation
                request.body =
                    "{\"Data\": {\"ticketID\": \"${ticket.id}\", \"lessonAvailabilityID\": \"${lesson.lessonAvailabilityID}\" }}"
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
                    runOnUiThread {
                        hideLoading()
                        noticeDialog(
                            this,
                            "예약 확인",
                            "${result.statusCode}\n${result.errorMessage}\n예약 취소에 실패 습니다"
                        )
                    }
                    return@request
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        201 -> {
                            hideLoading()
                            val intent = Intent(this, CompletionActivity::class.java)
                            intent.putExtra("data", 0)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
                RequestResult.failure -> {
                    if (result.errorMessage === "timeout") {
                        runOnUiThread { showNetworkDelay() }
                    } else {
                        runOnUiThread { showUnknownError() }
                    }
                    runOnUiThread { hideLoading() }
                }
                else -> {
                    runOnUiThread {
                        hideLoading()
                        showUnknownError()
                    }
                    return@request
                }
            }

        }
    }

    private fun reservePlate(body: HappyGolfGoRequest) {
        allHide()
        showLoading()

        requestManager.request(body, userManager.user) { result, json ->

            when (result) {
                RequestResult.nSuccess -> {
                    runOnUiThread { hideLoading() }
                    return@request
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        200 -> {
                            val item = json?.get("PlateReservation") as LinkedTreeMap<String, Any>
                            val policyData = json?.get("policy") as LinkedTreeMap<String, Any>
                            plateCancel = PlateReservation.create(item)
                            policy = Policy.create(policyData)

                            runOnUiThread {
                                cancelInitData()
                                hideLoading()
                            }
                        }
                    }
                }
                RequestResult.failure -> {
                    if (result.errorMessage === "timeout") {
                        runOnUiThread { showNetworkDelay() }
                    } else {
                        runOnUiThread { showUnknownError() }
                    }
                    runOnUiThread { hideLoading() }
                }
                else -> {
                    runOnUiThread {
                        showUnknownError()
                        hideLoading()
                    }
                    return@request
                }
            }

        }
    }

    private fun reserveLesson(body: HappyGolfGoRequest) {
        allHide()
        showLoading()

        requestManager.request(body, userManager.user) { result, json ->

            when (result) {
                RequestResult.nSuccess -> {
                    runOnUiThread { hideLoading() }
                    return@request
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        200 -> {
                            val item = json?.get("LessonReservation") as LinkedTreeMap<String, Any>
                            lessonCancel = LessonReservation.create(item)

                            runOnUiThread {
                                hideLoading()
                                cancelInitData()
                            }
                        }
                    }
                }
                RequestResult.failure -> {
                    if (result.errorMessage === "timeout") {
                        runOnUiThread { showNetworkDelay() }
                    } else {
                        runOnUiThread { showUnknownError() }
                    }
                    runOnUiThread { hideLoading() }
                }
                else -> {
                    runOnUiThread {
                        hideLoading()
                        showUnknownError()
                    }
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
                    runOnUiThread {
                        hideLoading()
                        noticeDialog(
                            this,
                            "예약 확인",
                            "${result.statusCode}\n${result.errorMessage}\n예약에 실패 하였습니다"
                        )
                    }
                    return@request
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        201 -> {
                            hideLoading()
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
                    if (result.errorMessage === "timeout") {
                        runOnUiThread { showNetworkDelay() }
                    } else {
                        runOnUiThread { showUnknownError() }
                    }
                    runOnUiThread { hideLoading() }
                }
                else -> {
                    runOnUiThread {
                        hideLoading()
                        showUnknownError()
                    }
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
                    runOnUiThread {
                        hideLoading()
                        noticeDialog(
                            this,
                            "예약 확인",
                            "${result.statusCode}\n${result.errorMessage}\n예약에 실패 하였습니다"
                        )
                    }
                    return@request
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        201 -> {
                            hideLoading()

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
                    if (result.errorMessage === "timeout") {
                        runOnUiThread { showNetworkDelay() }
                    } else {
                        runOnUiThread { showUnknownError() }
                    }
                    runOnUiThread { hideLoading() }
                }
                else -> {
                    runOnUiThread {
                        hideLoading()
                        showUnknownError()
                    }
                    return@request
                }
            }

        }
    }

    private fun platePolicyData() {
        allHide()
        showLoading()

        request = HappyGolfGoRequest.platePolicy
        requestManager.request(request, userManager.user) { result, json ->

            when (result) {
                RequestResult.nSuccess -> {
                    runOnUiThread {
                        showUnknownError()
                        hideLoading()
                    }
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        201 -> {
                            val policyData = json?.get("policy") as LinkedTreeMap<String, Any>
                            policy = Policy.create(policyData)

                            runOnUiThread {
                                reservePlatePolicy()
                                hideLoading()
                            }
                        }
                    }
                }
                RequestResult.failure -> {
                    if (result.errorMessage === "timeout") {
                        runOnUiThread { showNetworkDelay() }
                    } else {
                        runOnUiThread { showUnknownError() }
                    }
                    runOnUiThread { hideLoading() }
                }
                else -> {
                    runOnUiThread {
                        showUnknownError()
                        hideLoading()
                    }
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