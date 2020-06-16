package com.artilearn.happygolfgo.models

import com.artilearn.happygolfgo.enummodel.ClassType
import com.artilearn.happygolfgo.enummodel.ReservationStatus
import com.google.gson.internal.LinkedTreeMap
import java.io.Serializable
import java.util.*

data class PlateReservation(
    val id: Int,
    val name: String,
    val status: Int,
    val startDate: String,
    val endDate: String,
    val plateNumber: Int,
    val totalCount: Int,
    val remainingCount: Int,
    val isUnlimited: Boolean,
    val isCancellable: Boolean?,
    val type: ClassType.ModelType
) : Serializable {

    companion object {
        fun create(json: LinkedTreeMap<String, Any>): PlateReservation? {
            val id = (json["id"] as Double).toInt()
            check(id != null) {
                return null
            }
            val name = json["name"] as? String
            check(name != null) {
                return null
            }
            val statusInteger = (json["status"] as Double).toInt()
            check(statusInteger != null) {
                return null
            }
            val status = (json["status"] as Double).toInt()

            val startDate = json["startDate"] as String
            val endDate = json["endDate"] as String
            val plateNumber = (json["plateNumber"] as Double).toInt()
            val totalCount = (json["totalCount"] as Double).toInt()
            val remainingCount = (json["remainingCount"] as Double).toInt()
            val isUnlimited = json["isUnlimited"] as Boolean
            val isCancellable = json["isCancellable"] as Boolean?
            val type = ClassType.ModelType.PLATE_CANCEL

            return PlateReservation(
                id,
                name,
                status,
                startDate,
                endDate,
                plateNumber,
                totalCount,
                remainingCount,
                isUnlimited,
                isCancellable,
                type
            )
        }
    }
}