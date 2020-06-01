package com.artilearn.happygolfgo.ui.user

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.ui.BaseActivity
import com.artilearn.happygolfgo.ui.SignInActivity
import com.artilearn.happygolfgo.util.noticeDialog
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult
import kotlinx.android.synthetic.main.activity_pw_re_set.*
import kotlinx.android.synthetic.main.activity_verify_phone_number.include_toolbar
import kotlinx.android.synthetic.main.partial_bottom_button.view.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*

class PwReSetActivity : BaseActivity() {

    private lateinit var requestManager: RequestManager
    private lateinit var userManager: HappyGolfGoUserManager

    private val pattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&]).{8,15}.\$" // 특수문자 포함
    private var pw: String? = null
    private var check: Boolean = false
    private var phone: String? = null
    private var join = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pw_re_set)

        initView()
    }

    override fun bottomButtonAction() {
        super.bottomButtonAction()
        if (check) updatePw()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::requestManager.isInitialized) requestManager.rootActivity.finish()
    }

    private fun initView() {
        requestManager = RequestManager(this)
        userManager = HappyGolfGoUserManager(this)

        setUpActionBar(include_toolbar.toolbar, "비밀번호 재설정", true)
        setUpBottomButton(include_bottom.layout_bottom_button, "확인")

        phone = intent.getStringExtra("phone")
        join = intent.getIntExtra("where", 0)

        checkPw()
    }

    private fun checkPw() {
        et_pw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.matches(pattern.toRegex())) et_pw.error = "특수문자를 포함할 수 없습니다."

                if (start in 7..14) pw = s.toString()
                else et_pw.error = "영문과 숫자를 포함해 8자리 이상을 입력해주세요."
            }
        })

        et_checkPw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.toString() == pw) check = true
                else et_checkPw.error = "비밀번호가 일치하지 않습니다."
            }
        })
    }

    private fun updatePw() {
        val request = HappyGolfGoRequest.pwUpdate
        request.body =
            "{\"Data\": {\"phoneNumber\": \"${"$phone"}\", \"password\": \"${"$pw"}\" }}"
        requestManager.request(request, userManager.user) { result, json ->
            when (result) {
                RequestResult.nSuccess -> {
                    runOnUiThread { noticeDialog(this, "비밀번호 재설정", "비밀번호 변경에 실패했습니다.") }
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        201 -> {
                            runOnUiThread { alramDialog() }
                        }
                    }
                }
                RequestResult.failure -> {
                    runOnUiThread { noticeDialog(this, "비밀번호 재설정", "비밀번호 변경에 실패했습니다.") }
                }
                else -> {
                    runOnUiThread { noticeDialog(this, "비밀번호 재설정", "비밀번호 변경에 실패했습니다.") }
                }
            }
        }
    }

    private fun alramDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("비밀번호 재설정")
        builder.setMessage("변경이 완료되었습니다.")
        builder.setPositiveButton("확인") { _, _ ->
            startActivity()
        }

        builder.show()
    }

    private fun startActivity() {
        when (join) {
            100 -> { singInActivity() }
            101 -> { mainActivity() }
            else -> { mainActivity() }
        }
    }

    private fun singInActivity() {
        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun mainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}
