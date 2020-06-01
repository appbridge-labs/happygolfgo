package com.artilearn.happygolfgo.models

import com.artilearn.happygolfgo.enummodel.TicketType
import com.google.gson.internal.LinkedTreeMap
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Ticket(
    val id: Int,
    val name: String,
    val type: TicketType,
    val period: Int,
    val totalCount: Int,
    val usedCount: Int,
    val remainingCount: Int,
    val isUnlimited: Boolean,
    val onlyOnceToday: Boolean,
    val startDate: String,
    val endDate: String,
    val allowToBook: Boolean?

) : Serializable {

    companion object {
        fun create(json: LinkedTreeMap<String, Any>): Ticket? {
            val id = (json["id"] as Double).toInt()
            val name = json["name"] as String
            val typeInt = (json["type"] as Double).toInt()
            val type = typeInt.let { TicketType.create(it) }
            val period = (json["period"] as Double).toInt()
            val totalCount = (json["totalCount"] as Double).toInt()
            val usedCount = (json["usedCount"] as Double).toInt()
            val remainingCount = (json["remainingCount"] as Double).toInt()
            val isUnlimited = json["isUnlimited"] as Boolean
            val onlyOnceToday = json["onlyOnceToday"] as Boolean
            val startDate = json["startDate"] as String
            val endDate = json["endDate"] as String
            val allowToBook = json["allowToBook"] as Boolean?

            return Ticket(
                id,
                name,
                type,
                period,
                totalCount,
                usedCount,
                remainingCount,
                isUnlimited,
                onlyOnceToday,
                startDate,
                endDate,
                allowToBook
            )
        }
    }
}