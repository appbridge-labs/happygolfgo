package com.artilearn.happygolfgo.models

import com.google.gson.internal.LinkedTreeMap
import java.io.Serializable

data class LessonReviewDetail(
    val id: Int,
    val body: String,
    val comment: String?,
    val videoURL: String?,
    val thumbnailURL: String?,
    val ratings: Int?,
    val isReviewed: Boolean,
    val isConfirmed: Boolean,
    val confirmedDate: String?,
    val createdAt: String
) : Serializable {

    companion object {
        fun create(json: LinkedTreeMap<String, Any>): LessonReviewDetail? {
            val id = (json["id"] as Double).toInt()
            val body = json["body"] as String
            val comment = json["comment"] as String?
            val videoURL = json["videoURL"] as String?
            val thumbnailURL = json["thumbnailURL"] as String?
            val ratings = (json["ratings"] as Double?)?.toInt()
            val isReviewed = json["isReviewed"] as Boolean
            val isConfirmed = json["isConfirmed"] as Boolean
            val confirmedDate = json["confirmedDate"] as String?
            val createdAt = json["createdAt"] as String

            return LessonReviewDetail(
                id,
                body,
                comment,
                videoURL,
                thumbnailURL,
                ratings,
                isReviewed,
                isConfirmed,
                confirmedDate,
                createdAt
            )
        }
    }
}