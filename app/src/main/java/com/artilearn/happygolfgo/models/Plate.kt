package com.artilearn.happygolfgo.models

import com.artilearn.happygolfgo.enummodel.ClassType
import com.artilearn.happygolfgo.enummodel.PlateStatus
import com.google.gson.internal.LinkedTreeMap
import java.io.Serializable

data class Plate(
    var id: Int,
    var number: Int,
    var status: PlateStatus,
    var plateAvailability: Int,
    val type: ClassType.ModelType
) : Serializable {

    companion object {
        fun create(json: LinkedTreeMap<String, Any>): Plate? {
            var id = (json.get("id") as Double).toInt()
            check(id != null) {
                return null
            }
            var number = (json.get("number") as Double).toInt()
            check(number != null) {
                return null
            }
            var statusInteger = (json.get("status") as Double).toInt()
            check(statusInteger != null) {
                return null
            }
            var status = PlateStatus.create(statusInteger)
            var plateAvailabilityJSON = json.get("PlateAvailability") as? LinkedTreeMap<String, Any>

            val plateAvailability = (json["plateAvailabilityID"] as Double).toInt()
            val type = ClassType.ModelType.PLATE_RESERVE

            return Plate(
                id,
                number,
                status,
                plateAvailability,
                type
            )
        }
    }

}