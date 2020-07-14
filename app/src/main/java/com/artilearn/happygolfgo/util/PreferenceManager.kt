package com.artilearn.happygolfgo.util

import android.content.Context
import android.content.SharedPreferences
import com.artilearn.happygolfgo.constants.*

class PreferenceManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(HAPPY_GOLF_USER, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    var tutorial: Boolean
        get() = prefs.getBoolean(TUTORIAL, false)
        set(value) = editor.putBoolean(TUTORIAL, value).apply()

    var autoLogin: Boolean
        get() = prefs.getBoolean(AUTO_LOGIN, false)
        set(value) = editor.putBoolean(AUTO_LOGIN, value).apply()

    var fcmToken: String?
    get() = prefs.getString(FCM_TOKEN, "") ?: ""
    set(value) = editor.putString(FCM_TOKEN, value).apply()

    var accessToken: String
        get() = prefs.getString(ACCESS_TOKEN, "") ?: ""
        set(value) = editor.putString(ACCESS_TOKEN, value).apply()

    var clientScreetKey: String
        get() = prefs.getString(CLIENT_SECRETKEY, "") ?: ""
        set(value) = editor.putString(CLIENT_SECRETKEY, value).apply()

    var phoneNumber: String
        get() = prefs.getString(PHONE_NUMBER, "") ?: ""
        set(value) = editor.putString(PHONE_NUMBER, value).apply()

    var name: String
        get() = prefs.getString(NAME, "") ?: ""
        set(value) = editor.putString(NAME, value).apply()

    var profileImageURL: String?
        get() = prefs.getString(PROFILE_IMAGE, "")
        set(value) = editor.putString(PROFILE_IMAGE, value).apply()

    var nickName: String?
        get() = prefs.getString(NICK_NAME, "") ?: ""
        set(value) = editor.putString(NICK_NAME, value).apply()

    var dateOfBirth: String
    get() = prefs.getString(DATE_OF_BIRTH, "") ?: ""
    set(value) = editor.putString(DATE_OF_BIRTH, value).apply()

    var environmentMode: String
        get() = prefs.getString(DEFAULT_MODE, DEFAULT_MODE) ?: ""
        set(value) = editor.putString(DEFAULT_MODE, value).apply()

    var appVersion: String
        get() = prefs.getString(APP_VERSION, "1.0.0") ?: ""
        set(value) = editor.putString(APP_VERSION, value).apply()

    fun userLogout() {
        with(editor) {
            remove(NAME).apply()
            remove(FCM_TOKEN).apply()
            remove(CLIENT_SECRETKEY).apply()
            remove(AUTO_LOGIN).apply()
            remove(ACCESS_TOKEN).apply()
        }
    }

}