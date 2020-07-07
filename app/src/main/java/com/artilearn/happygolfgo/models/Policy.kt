package com.artilearn.happygolfgo.models

import com.google.gson.internal.LinkedTreeMap
import java.io.Serializable

data class Policy(
    val title: String,
    val body: String
) : Serializable {

    companion object {
        fun create(json: LinkedTreeMap<String, Any>): Policy? {
            val title = json["title"] as String
            val body = json["body"] as String

            return Policy(title, body)
        }
    }
}