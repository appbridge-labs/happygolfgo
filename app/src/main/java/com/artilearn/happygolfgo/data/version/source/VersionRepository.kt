package com.artilearn.happygolfgo.data.version.source

import com.artilearn.happygolfgo.data.version.LogoutModel
import com.artilearn.happygolfgo.data.version.VersionResponse
import io.reactivex.Completable
import io.reactivex.Single

interface VersionRepository {
    val name: String
    val nickName: String?
    val profileImage: String?

    fun getAppVersion(
        platform: Int = 1,
        clinentType: Int = 0
    ): Single<VersionResponse>

    fun userLogout(
        data: HashMap<String, LogoutModel>
    ): Completable
}