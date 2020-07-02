package com.artilearn.happygolfgo.models

import com.google.gson.internal.LinkedTreeMap
import java.io.Serializable

data class LessonReviewList(
    val id: Int,
    val createdAt: String,
    val employeeName: String
) : Serializable {

    companion object {
        fun create(json: LinkedTreeMap<String, Any>): LessonReviewList? {
            val id = (json["id"] as Double).toInt()
            val createdAt = json["createdAt"] as String
            val employeeName = json["employeeName"] as String

            return LessonReviewList(
                id, createdAt, employeeName
            )
        }
    }
}