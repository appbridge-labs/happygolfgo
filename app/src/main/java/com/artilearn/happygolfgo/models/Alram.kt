package com.artilearn.happygolfgo.models

import com.google.gson.internal.LinkedTreeMap
import java.io.Serializable

data class Alram(
    val id: Int,
    val title: String,
    val body: String,
    val type: Int,
    val createdAt: String
) : Serializable {

    companion object {
        fun create(json: LinkedTreeMap<String, Any>): Alram? {
            val id = (json["id"] as Double).toInt()
            val title = json["title"] as String
            val body = json["body"] as String
            val type = (json["type"] as Double).toInt()
            val createdAt = json["createdAt"] as String

            return Alram(
                id,
                title,
                body,
                type,
                createdAt
            )
        }
    }
}