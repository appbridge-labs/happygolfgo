package com.artilearn.happygolfgo.data.splash

data class SynchronizedDataModel(
    val clientSecretKey: String,
    val fcmToken: String,
    val isPushOn: Int,
    val operatingSystem: Int = 1,
    val clientType: Int = 0
)