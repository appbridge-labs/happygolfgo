package com.artilearn.happygolfgo.data.version.source.local

import com.artilearn.happygolfgo.util.PreferenceManager

class VersionLocalDataSourceImpl(
    private val preferenceManager: PreferenceManager
) : VersionLocalDataSource {

    override val name: String
        get() = preferenceManager.name

    override val nickName: String?
        get() = preferenceManager.nickName

    override val profileImage: String?
        get() = preferenceManager.profileImageURL

    override var appVersion: String
        get() = preferenceManager.appVersion
        set(value) {
            preferenceManager.appVersion = value
        }
}