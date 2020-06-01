package com.artilearn.happygolfgo.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.models.HappyGolfGoUser
import com.artilearn.happygolfgo.models.User
import com.artilearn.happygolfgo.ui.fragment.DeveloperFragment
import com.artilearn.happygolfgo.ui.user.MainActivity
import com.artilearn.happygolfgo.ui.user.PhoneAuthActivity
import com.artilearn.happygolfgo.util.SharedPreferencesHelper
import com.artilearn.happygolfgo.util.notificationCheck
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.internal.LinkedTreeMap
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : BaseActivity() {

    private lateinit var requestManager: RequestManager
    private lateinit var sp: SharedPreferencesHelper
    private lateinit var phone: EditText
    private lateinit var pw: EditText

    private var loginType = 1
    private var pushCheck = 0
    private var click = 0
    private var phoneNumber: String? = null
    private var password: String? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initView()
        initClickListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::requestManager.isInitialized) requestManager.rootActivity.finish()
    }

    private fun isLoading(loading: Boolean) {
        progressBar.visibility = if (loading) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun initView() {
        sp = SharedPreferencesHelper
        pushCheck = notificationCheck(this)
        requestManager = RequestManager(this)
        sp.init(this)

        phone = findViewById(R.id.et_phone)
        pw = findViewById(R.id.et_pw)

        getToken()
    }

    private fun getToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) return@OnCompleteListener

                token = task.result?.token
            })
    }

    private fun initClickListener() {
        textLenthListener()

        button_find_password_sign_in.setOnClickListener {
            val intent = Intent(this, PhoneAuthActivity::class.java)
            intent.putExtra("where", 100)
            startActivity(intent)
        }

        button_sign_in_sign_in.setOnClickListener {
            phoneNumber = phone.text.toString()
            password = pw.text.toString()

            when (loginType) {
                0 -> {

                }
                1 -> {
                    if (phoneNumber.isNullOrEmpty() && password.isNullOrEmpty()) {
                        isEmpty()
                    } else if (phoneNumber.isNullOrEmpty()) {
                        isEmptyPhone()
                    } else if (password.isNullOrEmpty()) {
                        isEmptyPw()
                    } else {
                        clientSignIn(phoneNumber!!, password!!)
                    }
                }
            }
        }

        image_view_go_sign_in.setOnClickListener {
            click++
            if (click >= 10) {
                click = 0
                val bottom = DeveloperFragment()
                bottom.show(supportFragmentManager, bottom.tag)
            }
        }
    }

    private fun isEmpty() {
        tv_emptyPhone.visibility = View.VISIBLE
        tv_emptyPw.visibility = View.VISIBLE
        tv_emptyPhone.text = "전화번호를 입력해 주세요"
        tv_emptyPw.text = "비밀번호를 입력해 주세요"
    }

    private fun isEmptyPhone() {
        tv_emptyPhone.visibility = View.VISIBLE
        tv_emptyPhone.text = "전화번호를 입력해 주세요"
    }

    private fun isEmptyPw() {
        tv_emptyPw.visibility = View.VISIBLE
        tv_emptyPw.text = "비밀번호를 입력해 주세요"
    }

    private fun isEmptyUser() {
        tv_emptyPhone.visibility = View.VISIBLE
        tv_emptyPhone.text = "등록되지 않은 번호 입니다"
    }

    private fun isFailPw() {
        tv_emptyPw.visibility = View.VISIBLE
        tv_emptyPw.text = "비밀번호를 확인해 주세요"
    }

    private fun startActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun textLenthListener() {
        phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_emptyPhone.visibility = View.INVISIBLE
            }
        })

        pw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_emptyPw.visibility = View.INVISIBLE
            }
        })
    }

    private fun clientSignIn(phoneNumber: String, password: String) {
        isLoading(true)

        val userManager = HappyGolfGoUserManager(this)
        val request = HappyGolfGoRequest.clientSignIn
        request.body =
            "{\"Data\": {\"phoneNumber\": \"${phoneNumber}\", \"operatingSystem\": \"1\", \"password\": \"${password}\", \"fcmToken\":\"${token}\", \"sysLang\": 0 }}"
        requestManager.request(request, null) { result, json ->
            when (result) {
                RequestResult.nSuccess -> {
                    runOnUiThread {
                        when ((json?.get("resultCode") as Double).toInt()) {
                            4003 -> {
                                isEmptyUser()
                            }
                            4004 -> {
                                isFailPw()
                            }
                        }
                        isLoading(false)
                    }
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        200 -> {
                            runOnUiThread { isLoading(false) }

                            val userJSON = json?.get("User") as LinkedTreeMap<String, Any>
                            val save = User.create(userJSON)
                            userManager.user = HappyGolfGoUser(userJSON)

                            save?.let {
                                with(it) {
                                    sp.setUserType(type)
                                    sp.setFcmToken(fcmToken)
                                    sp.setPushOn(pushCheck)
                                    sp.setClientSecretKey(clientSecretKey)
                                    sp.setPhoneNumber(phoneNumber)
                                    sp.setName(name)
                                    sp.setNickName(nickname)
                                    sp.setAccessToken(accessToken)
                                }
                            }
                            startActivity()
                        }
                    }
                }
                RequestResult.failure -> {
                    runOnUiThread {
                        isLoading(false)
                        showToast("인터넷 환경이 좋지 않습니다")
                    }
                }
                else -> {
                    runOnUiThread {
                        showToast("서버 상태가 좋지 않습니다")
                        isLoading(false)
                    }
                }
            }
        }
    }
}