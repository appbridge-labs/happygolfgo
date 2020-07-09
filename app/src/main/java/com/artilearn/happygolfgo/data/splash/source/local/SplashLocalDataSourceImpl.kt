package com.artilearn.happygolfgo.data.splash.source.local

import com.artilearn.happygolfgo.util.PreferenceManager

class SplashLocalDataSourceImpl(
    private val preferenceManager: PreferenceManager
) : SplashLocalDataSource {

    override var clientSecretKey: String
        get() = preferenceManager.clientScreetKey
        set(value) {
            preferenceManager.clientScreetKey = value
        }
}