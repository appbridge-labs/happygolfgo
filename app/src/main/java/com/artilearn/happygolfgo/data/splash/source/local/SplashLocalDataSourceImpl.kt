package com.artilearn.happygolfgo.data.splash.source.local

import com.artilearn.happygolfgo.util.PreferenceManager

class SplashLocalDataSourceImpl(
    private val preferenceManager: PreferenceManager
) : SplashLocalDataSource {

    override val autoLogin: Boolean
        get() = preferenceManager.autoLogin

    override val tutorial: Boolean
        get() = preferenceManager.tutorial

    override var clientSecretKey: String
        get() = preferenceManager.clientScreetKey
        set(value) {
            preferenceManager.clientScreetKey = value
        }
}