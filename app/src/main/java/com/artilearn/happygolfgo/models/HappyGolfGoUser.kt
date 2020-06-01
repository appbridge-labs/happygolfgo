package com.artilearn.happygolfgo.models

import com.google.gson.internal.LinkedTreeMap
import com.tlsolution.tlsmodules.UserManager.User


class HappyGolfGoUser : User {
    override var id: Int
    override var clientSecret: String?
    override var token: String?
    var name: String
    var nickname: String?
    var branchID: Int
    override var type: Int?

    constructor(json: LinkedTreeMap<String, Any>) {

        this.id = (json?.get("id") as Double).toInt()
        this.clientSecret = json?.get("clientSecretKey") as String
        this.token = json?.get("accessToken") as String
        this.name = json?.get("name") as String
        this.branchID = (json?.get("branchID") as Double).toInt()
        this.nickname = json?.get("nickname") as String?
        this.type = 1
    }

    constructor(
        id: Int,
        clientSecret: String?,
        token: String?,
        name: String,
        nickname: String?,
        branchID: Int
    ) {
        this.id = id
        this.clientSecret = clientSecret
        this.token = token
        this.type = 1
        this.name = name
        this.nickname = nickname
        this.branchID = branchID

    }

    override var json: String? = null
        get() {
            val idHash = if (id != null) "\"id\":\"$id\"," else ""
            val clientSecretHash =
                if (clientSecret != null) "\"clientSecretKey\":\"$clientSecret\"," else ""
            val tokenHash = if (token != null) "\"accessToken\":\"$token\"," else ""
            val nameHash = if (name != null) "\"name\":\"$name\"," else ""
            val nicknameHash = if (nickname != null) "\"nickname\":\"$nickname\"," else ""
            val branchIDHash = if (branchID != null) "\"currentBranchID\":\"$branchID\"" else ""

            return "{$idHash $clientSecretHash $tokenHash $nameHash $nicknameHash $branchIDHash}"
        }

}