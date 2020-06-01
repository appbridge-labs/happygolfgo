package com.artilearn.happygolfgo.models

import com.artilearn.happygolfgo.enummodel.ClassType
import com.google.gson.internal.LinkedTreeMap
import java.io.Serializable


data class LessonReservation(
    val id: Int,
    val name: String,
    val status: Int,
    val startDate: String,
    val endDate: String,
    val totalCount: Int,
    val remainingCount: Int,
    val isUnlimited: Boolean,
    val type: ClassType.ModelType
) : Serializable {
    companion object {
        fun create(json: LinkedTreeMap<String, Any>): LessonReservation? {

            val id = (json.get("id") as Double).toInt()
            val name = json.get("name") as String
            val status = (json.get("status") as Double).toInt()
            val startDate = json.get("startDate") as String
            val endDate = json.get("endDate") as String
            val totalCount = (json.get("totalCount") as Double).toInt()
            val remainingCount = (json.get("remainingCount") as Double).toInt()
            val isUnlimited = json.get("isUnlimited") as Boolean
            val type = ClassType.ModelType.LESSON_CANCEL

            return LessonReservation(
                id,
                name,
                status,
                startDate,
                endDate,
                totalCount,
                remainingCount,
                isUnlimited,
                type
            )
        }
    }
}