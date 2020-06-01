package com.artilearn.happygolfgo.models

import com.google.gson.internal.LinkedTreeMap
import java.io.Serializable
import java.util.*

data class PlateTime(
    val plateAvailabilityID: Int,
    val startDate: String,
    val endDate: String,
    val totalCount: Int,
    val availableCount: Int
) : Serializable {

    companion object {
        fun create(json: LinkedTreeMap<String, Any>): PlateTime {
            val plateAvailabilityID = (json["plateAvailabilityID"] as Double).toInt()
            val startDate = json["startDate"] as String
            val endDate = json["endDate"] as String
            val totalCount = (json["totalCount"] as Double).toInt()
            val availableCount = (json["availableCount"] as Double).toInt()

            return PlateTime(
                plateAvailabilityID,
                startDate,
                endDate,
                totalCount,
                availableCount
            )
        }
    }
}