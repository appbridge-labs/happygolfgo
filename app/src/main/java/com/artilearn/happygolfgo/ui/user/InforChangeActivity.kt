package com.artilearn.happygolfgo.ui.user

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.ui.BaseActivity
import com.artilearn.happygolfgo.util.SharedPreferencesHelper
import com.artilearn.happygolfgo.util.noticeDialog
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult
import kotlinx.android.synthetic.main.activity_infor_change.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*
import java.util.regex.Pattern

class InforChangeActivity : BaseActivity() {

    private lateinit var sp: SharedPreferencesHelper
    private lateinit var requestManager: RequestManager
    private lateinit var userManager: HappyGolfGoUserManager

    private var nick: String? = null
    private var check = false
    private var nickName: String? = null
    private val pattern = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝|]*"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infor_change)

        initView()
        topBottom()
    }

    override fun bottomButtonAction() {
        super.bottomButtonAction()
        if (check) updateNickName()
        else noticeDialog(this, "닉네임 변경", "닉네임을 확인해 주세요.")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::requestManager.isInitialized) requestManager.rootActivity.finish()
    }

    private fun initView() {
        sp = SharedPreferencesHelper
        sp.init(this)
        requestManager = RequestManager(this)
        userManager = HappyGolfGoUserManager(this)

        nick = sp.getNickName()

        et_name.setText(sp.getName())
        et_nick.hint = nick

        onCheckListener()
    }

    private fun onCheckListener() {
        et_nick.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val nick = s.toString().trim()

                when {
                    nick.isEmpty() -> {
                        check = false
                        et_nick.error = "2글자 이상 입력해주세요."
                    }
                    nick.length in 2..8 -> {
                        check = true
                        nickName = nick
                    }
                    else -> {
                        if (!Pattern.matches(pattern, nick)) et_nick.error = "특수문자는 포함할 수 없습니다."
                        check = false
                        et_nick.error = "2글자 이상 8글자 이하로 입력해 주세요."
                    }
                }
            }
        })
    }

    private fun topBottom() {
        setUpActionBar(
            toolbar_my_page.toolbar,
            "개인정보 변경",
            true
        )

        setUpBottomButton(
            bottom_my_page,
            "확인"
        )
    }

    private fun isLoading(loading: Boolean) {
        progressBar.visibility = if (loading) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun updateNickName() {
        isLoading(true)

        val request = HappyGolfGoRequest.nickNameUpdate
        request.body =
            "{\"Data\": {\"nickname\": \"${"$nickName"}\" }}"
        requestManager.request(request, userManager.user) { result, json ->
            when (result) {
                RequestResult.nSuccess -> {
                    runOnUiThread { noticeDialog(this, "닉네임 변경", "닉네임 변경에 실패했습니다.") }
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        200 -> {
                            runOnUiThread {
                                isLoading(false)
                                alramDialog()
                            }
                        }
                    }
                }
                RequestResult.failure -> {
                    runOnUiThread { noticeDialog(this, "닉네임 변경", "닉네임 변경에 실패했습니다.") }
                }
                else -> {
                    runOnUiThread { noticeDialog(this, "닉네임 변경", "닉네임 변경에 실패했습니다.") }
                }
            }
        }
    }

    private fun alramDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("닉네임 변경")
        builder.setMessage("닉네임 변경이 완료되었습니다.")
        builder.setPositiveButton("확인") { _, _ ->
            startActivity()
        }

        builder.show()
    }

    private fun startActivity() {
        sp.setNickName(nickName!!)

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}
