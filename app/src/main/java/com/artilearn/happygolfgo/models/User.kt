package com.artilearn.happygolfgo.models

import com.google.gson.internal.LinkedTreeMap
import java.io.Serializable

data class User(
    val id: Int,
    val type: Int,
    val name: String,
    val profileImageURL: String?,
    val sex: Int?,
    val nickname: String?,
    val dateOfBirth: String?,
    val phoneNumber: String,
    val branchID: Int,
    val clientSecretKey: String?,
    val fcmToken: String?,
    val isLoggedIn: Int,
    val sysLang: Int,
    val memo: String?,
    val accessToken: String
) : Serializable {

    companion object {
        fun create(json: LinkedTreeMap<String, Any>): User? {
            val id = (json["id"] as Double).toInt()
            val type = (json["type"] as Double).toInt()
            val name = json["name"] as String
            val profileImageURL = json["profileImageURL"] as String?
            val sex = (json["sex"] as Double?)?.toInt()
            val nickname = json["nickname"] as String?
            val dateOfBirth = json["dateOfBirth"] as String?
            val phoneNumber = json["phoneNumber"] as String
            val branchID = (json["branchID"] as Double).toInt()
            val clientSecretKey = json["clientSecretKey"] as String?
            val fcmToken = json["fcmToken"] as String?
            val isLoggedIn = (json["isLoggedIn"] as Double).toInt()
            val sysLang = (json["sysLang"] as Double).toInt()
            val memo = json["memo"] as String?
            val accessToken = json["accessToken"] as String

            return User(
                id,
                type,
                name,
                profileImageURL,
                sex,
                nickname,
                dateOfBirth,
                phoneNumber,
                branchID,
                clientSecretKey,
                fcmToken,
                isLoggedIn,
                sysLang,
                memo,
                accessToken
            )
        }
    }
}