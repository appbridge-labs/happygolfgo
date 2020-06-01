package com.artilearn.happygolfgo.models

import com.artilearn.happygolfgo.enummodel.ClassType
import com.google.gson.internal.LinkedTreeMap
import java.io.Serializable

data class LessonTime(
    val lessonAvailabilityID: Int,
    val startDate: String,
    val endDate: String,
    val status: Int,
    val type: ClassType.ModelType
) : Serializable {

    companion object {
        fun create(json: LinkedTreeMap<String, Any>): LessonTime {
            val lessonAvailabilityID = (json["lessonAvailabilityID"] as Double).toInt()
            val startDate = json["startDate"] as String
            val endDate = json["endDate"] as String
            val status = (json["status"] as Double).toInt()
            val type = ClassType.ModelType.LESSON_RESERVE

            return LessonTime(
                lessonAvailabilityID,
                startDate,
                endDate,
                status,
                type
            )
        }
    }
}