package com.artilearn.happygolfgo.data.profilechange.source.local

import com.artilearn.happygolfgo.util.PreferenceManager

class ProfileChangeLocalDataSourceImpl(
    private val preferenceManager: PreferenceManager
) : ProfileChangeLocalDataSource {
    override val profileImage: String?
        get() = preferenceManager.profileImageURL
}