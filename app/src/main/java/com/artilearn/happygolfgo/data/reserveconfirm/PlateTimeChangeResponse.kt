package com.artilearn.happygolfgo.data.reserveconfirm

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlateTimeChangeResponse(
    @SerializedName("resultCode")
    @Expose
    val resultCode: Int,

    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Data")
    @Expose
    val data: PlateReservation
) {

    data class PlateReservation(
        @field:SerializedName("id")
        val id: Int,
        @field:SerializedName("name")
        val name: String,
        @field:SerializedName("status")
        val status: Int,
        @field:SerializedName("startDate")
        val startDate: String,
        @field:SerializedName("endDate")
        val endDate: String,
        @field:SerializedName("createdAt")
        val createdAt: String,
        @field:SerializedName("updatedAt")
        val updatedAt: String,
        @field:SerializedName("relatedPlateID")
        val relatedPlateID: Int,
        @field:SerializedName("branchID")
        val branchID: Int,
        @field:SerializedName("userID")
        val userID: Int,
        @field:SerializedName("ticketID")
        val ticketID: Int,
        @field:SerializedName("schduleID")
        val schduleID: Int,
        @field:SerializedName("RelatedPlate")
        val relatedPlate: RelatedPlate
    )

    data class RelatedPlate(
        @field:SerializedName("id")
        val id: Int,
        @field:SerializedName("number")
        val number: String,
        @field:SerializedName("status")
        val status: Int,
        @field:SerializedName("plateAvailabilityID")
        val plateAvailabilityID: Int,
        @field:SerializedName("PlateAvailability")
        val plateAvailability: PlateAvailability
    )

    data class PlateAvailability(
        @field:SerializedName("id")
        val id: Int,
        @field:SerializedName("startDate")
        val startDate: String,
        @field:SerializedName("endDate")
        val endDate: String,
        @field:SerializedName("name")
        val name: String,
        @field:SerializedName("memo")
        val memo: String,
        @field:SerializedName("plateID")
        val plateID: Int,
        @field:SerializedName("branchID")
        val branchID: Int
    )
}