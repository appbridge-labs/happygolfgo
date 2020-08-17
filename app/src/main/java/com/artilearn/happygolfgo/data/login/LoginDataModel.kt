package com.artilearn.happygolfgo.data.login

data class LoginDataModel(
    val phoneNumber: String,
    val password: String,
    val fcmToken: String?,
    val operatingSystem: Int = 1,
    val sysLang: Int = 0
)