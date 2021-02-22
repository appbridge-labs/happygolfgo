package com.artilearn.happygolfgo.data.profilechange.source

import com.artilearn.happygolfgo.data.profilechange.source.local.ProfileChangeLocalDataSource

class ProfileChangeRepositoryImpl(
    private val localDataSource: ProfileChangeLocalDataSource
) : ProfileChangeRepository {
    override val profileImage: String?
        get() = localDataSource.profileImage
}