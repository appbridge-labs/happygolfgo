package com.artilearn.happygolfgo.ui.user

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.ui.BaseActivity
import com.artilearn.happygolfgo.ui.SignInActivity
import com.artilearn.happygolfgo.util.SharedPreferencesHelper
import com.artilearn.happygolfgo.util.noticeDialog
import com.tlsolution.tlsmodules.Extensions.showTwoActionableAlert
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult
import kotlinx.android.synthetic.main.activity_my_page.*
import kotlinx.android.synthetic.main.partial_menu_with_right_arrow.view.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*


class MyPageActivity : BaseActivity() {
val TAG = javaClass.simpleName
    private lateinit var requestManager: RequestManager
    private lateinit var userManager: HappyGolfGoUserManager
    private lateinit var sp: SharedPreferencesHelper
    private var version: String? = null
    private var currentVersion: String? = null
    private var packageInfo: PackageInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        initView()
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

        currentVersion = nowAppVersion()

        setDisplay()
        setUpClickListener()
        loadAppVersion()
    }

    private fun setDisplay() {
        setUpActionBar(toolbar_my_page.toolbar, getString(R.string.toolbar_title_my_page), true)

        lesson.text_view_title.text = getString(R.string.mypage_lesson_title)
        infor.text_view_title.text = getString(R.string.text_view_title_change_my_information)
        password.text_view_title.text = getString(R.string.text_view_title_change_password)
        signout.text_view_title.text = getString(R.string.text_view_title_sign_out)
        currentVersion?.let {
            tv_version.text = it
        }

    }

    private fun setUpClickListener() {
        infor.setOnClickListener { changeInfor() }
        password.setOnClickListener { pwChange() }
        lesson.setOnClickListener { noticeDialog(this, "준비중", "레슨일지 준비 중 입니다") }
        signout.setOnClickListener {
            showTwoActionableAlert(
                getString(R.string.alert_title_sign_out),
                getString(R.string.alert_message_sign_out),
                getString(R.string.alert_choice_confrim),
                getString(R.string.alert_choice_cancel),
                { signOut() }
            )
        }
    }

    private fun isLoading(loading: Boolean) {
        progressBar.visibility = if (loading) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun userClear() {
        sp.userLogOut(
            "userType", "fcmToken", "isPush", "clientkey",
            "name", "phone", "nickname", "accessToken"
        )
    }

    private fun newStart() {
        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun changeInfor() {
        val intent = Intent(this, InforChangeActivity::class.java)
        startActivity(intent)
    }

    private fun pwChange() {
        val intent = Intent(this, PhoneAuthActivity::class.java)
        intent.putExtra("where", 101)
        startActivity(intent)
    }

    private fun signOut() {
        isLoading(true)

        val request = HappyGolfGoRequest.clientSignOut
        request.body = "{}"
        requestManager.request(request, userManager.user) { result, json ->
            when (result) {
                RequestResult.nSuccess -> {
                    runOnUiThread { isLoading(false) }
                    userClear()
                    newStart()
                }
                RequestResult.pSuccess -> {
                    runOnUiThread { isLoading(false) }
                    when (result.statusCode) {
                        201 -> {
                            userClear()
                            userManager.user = null
                            newStart()
                        }
                    }
                }
                RequestResult.failure -> {
                    runOnUiThread { isLoading(false) }
                    userClear()
                    newStart()
                }
                else -> {
                    runOnUiThread { isLoading(false) }
                    userClear()
                    newStart()
                }
            }

        }
    }

    private fun loadAppVersion() {
        val request = HappyGolfGoRequest.appVersion

        requestManager.request(request, userManager.user) { result, json ->
            when (result) {
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        200 -> {
                            version = json?.get("latestVersion") as String

                            runOnUiThread {
                                if (currentVersion != version) {
                                    tv_update.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private fun nowAppVersion(): String? {
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return packageInfo?.versionName
    }

}
