package com.artilearn.happygolfgo.util

import android.content.Context
import android.content.SharedPreferences


object SharedPreferencesHelper {

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    fun setTutorial(value: Int) {
        prefs.edit().putInt("tutorial", value).apply()
    }

    fun getTutorial(): Int {
        return prefs.getInt("tutorial", 0)
    }

    fun setUserType(value: Int) {
        prefs.edit().putInt("userType", value).apply()
    }

    fun getUserType(): Int {
        return prefs.getInt("userType", 10)
    }

    fun setFcmToken(value: String?) {
        value?.let { prefs.edit().putString("fcmToken", value).apply() }
    }

    fun getFcmToken(): String? {
        return prefs.getString("fcmToken", null)
    }

    fun setPushOn(value: Int) {
        prefs.edit().putInt("isPush", value).apply()
    }

    fun getPushOn(): Int {
        return prefs.getInt("isPush", 0)
    }

    fun setClientSecretKey(value: String?) {
        value.let { prefs.edit().putString("clientkey", value).apply() }
    }

    fun getClientSecretKey(): String? {
        return prefs.getString("clientkey", null)
    }

    fun setName(value: String) {
        prefs.edit().putString("name", value).apply()
    }

    fun getName(): String? {
        return prefs.getString("name", null)
    }

    fun setPhoneNumber(value: String) {
        prefs.edit().putString("phone", value).apply()
    }

    fun getPhoneNumber(): String? {
        return prefs.getString("phone", null)
    }

    fun setNickName(value: String?) {
        value?.let { prefs.edit().putString("nickname", value).apply() }
    }

    fun getNickName(): String? {
        return prefs.getString("nickname", null)
    }

    fun setAccessToken(value: String) {
        value.let { prefs.edit().putString("accessToken", value).apply() }
    }

    fun getAccessToken(): String {
        return prefs.getString("accessToken", "")!!
    }

    fun userLogOut(userType: String, fcmToken: String, isPush: String, clienKey: String,
                   name: String, phoneNumber: String, nickname: String, accessToken: String) {
        prefs.edit().remove(userType).apply()
        prefs.edit().remove(fcmToken).apply()
        prefs.edit().remove(isPush).apply()
        prefs.edit().remove(clienKey).apply()
        prefs.edit().remove(name).apply()
        prefs.edit().remove(phoneNumber).apply()
        prefs.edit().remove(nickname).apply()
        prefs.edit().remove(accessToken).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }


}