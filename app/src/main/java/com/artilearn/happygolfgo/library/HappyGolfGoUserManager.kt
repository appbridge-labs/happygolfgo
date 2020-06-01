package com.artilearn.happygolfgo.library

import android.content.Context
import android.util.Log
import com.artilearn.happygolfgo.models.HappyGolfGoUser
import com.google.gson.GsonBuilder
import com.tlsolution.tlsmodules.UserManager.User


class HappyGolfGoUserManager(context: Context) :
    com.tlsolution.tlsmodules.UserManager.UserManager(context) {

    companion object {
        val TAG = "HappyGolfGoUserManager"
    }

    override var user: User?
        get() {
            val userJson = sharedPref.getString(USER_KEY, null)
            check(userJson != null) { return null }
            Log.d(TAG, "$userJson")
            val json = GsonBuilder().create().fromJson(userJson, HashMap<String, Any>().javaClass)

            val id = (json?.get("id") as String).toInt()
            var name = json?.get("name") as String
            var branchID = (json?.get("currentBranchID") as String).toInt()
            var nickname = json?.get("nickname") as String?
            var clientSecret = json?.get("clientSecretKey") as String
            var token = json?.get("accessToken") as String

            var user = HappyGolfGoUser(id, clientSecret, token, name, nickname, branchID)
            return user

        }
        set(value) {
            val editor = sharedPref.edit()
            editor.putString(USER_KEY, value?.json)
            Log.d(TAG, "User info has been saved! ${value?.json}")
            editor.commit()
        }
}