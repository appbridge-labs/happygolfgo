package com.artilearn.happygolfgo.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.ui.BaseActivity
import com.artilearn.happygolfgo.util.noticeDialog
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult
import kotlinx.android.synthetic.main.activity_verify_phone_number.*
import kotlinx.android.synthetic.main.partial_text_input_view.view.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*
import java.util.regex.Pattern


class PhoneAuthActivity : BaseActivity() {

    private lateinit var requestManager: RequestManager
    private lateinit var userManager: HappyGolfGoUserManager

    private var isVerify = false
    private var isOk = false
    private var number: String? = null
    private var phone: String? = null
    private val type = 0
    private var join = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone_number)

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::requestManager.isInitialized) requestManager.rootActivity.finish()
    }

    private fun initView() {
        requestManager = RequestManager(this)
        userManager = HappyGolfGoUserManager(this)

        setUpActionBar(include_toolbar.toolbar, "비밀번호 재설정", true)

        include_phone.edit_text_input.inputType = 0x00000002
        include_pw.edit_text_input.inputType = 0x00000002

        include_phone.text_view_title.text = "전화번호"
        include_phone.edit_text_input.hint = "01000000000"
        include_pw.text_view_title.text = "인증번호"
        include_pw.edit_text_input.hint = "0000"

        btn_send.setOnClickListener {
            isLoading(true)
            phoneCheck()
        }

        btn_check.setOnClickListener {
            if (isVerify) setNumber()
            else noticeDialog(this, "인증번호", "인증번호를 생성해 주세요.")
        }

        join = intent.getIntExtra("where", 0)
    }

    private fun isLoading(loading: Boolean) {
        progressBar.visibility = if (loading) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun phoneCheck() {
        val pattern = "^[0-9]*\$"
        phone = include_phone.edit_text_input.text.toString()

        if (phone!!.isEmpty() || !Pattern.matches(pattern, phone)) {
            include_phone.edit_text_input.error = "번호를 입력해 주세요"
        } else if (phone!!.length > 11 || phone!!.length < 11) {
            include_phone.edit_text_input.error = "번호를 바르게 입력해 주세요"
        } else {
            getNumber()
        }
    }

    private fun getNumber() {
        val request = HappyGolfGoRequest.pwNumber
        request.body =
            "{\"Data\": {\"phoneNumber\": \"${"$phone"}\", \"clientType\": \"${"$type"}\" }}"
        requestManager.request(request, userManager.user) { result, json ->
            when (result) {
                RequestResult.nSuccess -> {
                    runOnUiThread {
                        isLoading(false)
                        noticeDialog(this, "인증번호", "인증번호 발송에 실패했습니다.")
                    }
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        200 -> {
                            isVerify = true
                            runOnUiThread {
                                isLoading(false)
                                noticeDialog(this, "인증번호", "인증번호를 발송했습니다.")
                            }
                        }
                    }
                }
                RequestResult.failure -> {
                    runOnUiThread {
                        isLoading(false)
                        noticeDialog(this, "인증번호", "인증번호 발송에 실패했습니다.")
                    }
                }
                else -> {
                    runOnUiThread {
                        isLoading(false)
                        noticeDialog(this, "인증번호", "인증번호 발송에 실패했습니다.")
                    }
                }
            }
        }
    }

    private fun setNumber() {
        isLoading(true)
        number = include_pw.edit_text_input.text.toString()

        val request = HappyGolfGoRequest.verifyNumber
        request.body =
            "{\"Data\": {\"phoneNumber\": \"${"$phone"}\", \"verificationNumber\": \"${"$number"}\" }}"
        requestManager.request(request, userManager.user) { result, json ->
            when (result) {
                RequestResult.nSuccess -> {
                    runOnUiThread {
                        isLoading(false)
                        noticeDialog(this, "인증번호", "인증번호가 틀립니다.")
                    }
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        200 -> {
                            isOk = true
                            runOnUiThread {
                                isLoading(false)
                                alramDialog()
                            }
                        }
                    }
                }
                RequestResult.failure -> {
                    runOnUiThread {
                        isLoading(false)
                        noticeDialog(this, "인증번호", "인증번호가 틀립니다.")
                    }
                }
                else -> {
                    runOnUiThread {
                        isLoading(false)
                        noticeDialog(this, "인증번호", "인증번호가 틀립니다.")
                    }
                }
            }
        }
    }

    private fun alramDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("인증번호")
        builder.setMessage("인증 완료 되었습니다.")
        builder.setPositiveButton("확인") { _, _ ->
            startActivity()
        }

        builder.show()
    }

    private fun startActivity() {
        val intent = Intent(this, PwReSetActivity::class.java)
        intent.putExtra("phone", phone)
        intent.putExtra("where", join)
        startActivity(intent)
        finish()
    }
}
