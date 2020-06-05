package com.artilearn.happygolfgo.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.library.HappyGolfGoCoachManager
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.models.User
import com.artilearn.happygolfgo.ui.user.MainActivity
import com.artilearn.happygolfgo.util.SharedPreferencesHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.internal.LinkedTreeMap
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult


class SplashActivity : AppCompatActivity() {
    private val TAG = "SplashActivity"

    private lateinit var userManager: HappyGolfGoUserManager
    private lateinit var coachManager: HappyGolfGoCoachManager
    private lateinit var request: HappyGolfGoRequest
    private lateinit var requestManager: RequestManager
    private lateinit var sp: SharedPreferencesHelper

    private var tutorial: Int? = null
    private var user: Int? = null
    private var clientKey: String? = null
    private var pushOn: Int? = null
    private var token: String? = null
    private var realToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::requestManager.isInitialized) requestManager.rootActivity.finish()
    }

    private fun initView() {
        userManager = HappyGolfGoUserManager(this)
        coachManager = HappyGolfGoCoachManager(this)
        requestManager = RequestManager(this)
        sp = SharedPreferencesHelper
        sp.init(this)

        with(sp) {
            tutorial = getTutorial()
            user = getUserType()
            clientKey = getClientSecretKey()
            pushOn = getPushOn()
            token = getFcmToken()
        }

        getToken()
        Handler().postDelayed({ mainActivityChoice() }, 1000L)
    }

    private fun getToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                realToken = task.result?.token
            })
    }

    private fun checkSynchronized() {
        request = HappyGolfGoRequest.userSynchronized
        request.body =
            "{\"Data\": {\"clientSecretKey\": \"${"$clientKey"}\", \"operatingSystem\": \"1\", \"clientType\": \"${"$user"}\", \"fcmToken\": \"${"$realToken"}\", \"isPushOn\": \"${"$pushOn"}\" }}"
        requestManager.request(request, userManager.user) { result, json ->
            when (result) {
                RequestResult.nSuccess -> {
                    startMain()
                    return@request
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        200 -> {
                            val userJSON = json?.get("user") as LinkedTreeMap<String, Any>
                            val save = User.create(userJSON)

                            save?.let {
                                SharedPreferencesHelper.setFcmToken(it.fcmToken)
                                SharedPreferencesHelper.setClientSecretKey(it.clientSecretKey)
                                SharedPreferencesHelper.setClientSecretKey(it.accessToken)
                            }

                            startMain()
                        }
                    }
                }
                RequestResult.failure -> {
                    startMain()
                }
                else -> {
                    startMain()
                }
            }
        }
    }

    private fun mainActivityChoice() {
        if (tutorial == 0) {
            val intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            when (user) {
                0 -> {

                }
                1 -> {
                    if (clientKey.isNullOrEmpty()) {
                        startSignIn()
                    } else {
                        checkSynchronized()
                    }
                }
                else -> {
                    startSignIn()
                }
            }
        }
    }

    private fun startSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
