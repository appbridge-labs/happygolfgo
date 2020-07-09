package com.artilearn.happygolfgo.data.splash.source.local

interface SplashLocalDataSource {

    var clientSecretKey: String
    val autoLogin: Boolean
    val tutorial: Boolean
}