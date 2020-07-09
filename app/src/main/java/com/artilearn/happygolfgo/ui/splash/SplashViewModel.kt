package com.artilearn.happygolfgo.ui.splash

import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.PreferenceManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class SplashViewModel(

) : BaseViewModel() {

    private val applicationContext = MyApplication.getApplicationContext()

    init {
        getToken()
    }

    private fun getToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                PreferenceManager(applicationContext).fcmToken = task.result?.token
            })
    }
}