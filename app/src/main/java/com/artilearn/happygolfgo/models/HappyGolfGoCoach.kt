package com.artilearn.happygolfgo.models

import com.google.gson.internal.LinkedTreeMap
import com.tlsolution.tlsmodules.UserManager.User


class HappyGolfGoCoach : User {
    override var id: Int
    override var clientSecret: String?
    override var token: String?
    var name: String
    override var type: Int?


    constructor(json: LinkedTreeMap<String, Any>) {

        this.id = (json?.get("id") as Double).toInt()
        this.clientSecret = json?.get("employeeSecretKey") as String
        this.token = json?.get("accessToken") as String
        this.name = json?.get("name") as String
        this.type = 0
    }

    constructor(
        id: Int,
        clientSecret: String?,
        token: String?,
        name: String
    ) {
        this.id = id
        this.clientSecret = clientSecret
        this.token = token
        this.name = name
        this.type = 0
    }

    override var json: String? = null
        get() {
            val idHash = if (id != null) "\"id\":\"$id\"," else ""
            val clientSecretHash =
                if (clientSecret != null) "\"clientSecretKey\":\"$clientSecret\"," else ""
            val tokenHash = if (token != null) "\"accessToken\":\"$token\"," else ""
            val nameHash = if (name != null) "\"name\":\"$name\"" else ""


            return "{$idHash $clientSecretHash $tokenHash $nameHash }"
        }
}