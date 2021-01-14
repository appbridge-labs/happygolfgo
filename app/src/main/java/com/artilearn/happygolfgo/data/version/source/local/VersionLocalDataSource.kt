package com.artilearn.happygolfgo.data.version.source.local

interface VersionLocalDataSource {

    var appVersion: String
    val name: String
    val nickName: String?
    val profileImage: String?
}