package com.artilearn.happygolfgo.enummodel

enum class ReservationStatus {
    reserved,
    checkedIn,
    absent,
    invalid;

    companion object CREATOR {
        fun create(integer: Int): ReservationStatus {
            when (integer) {
                0 -> return reserved
                1 -> return checkedIn
                2 -> return absent
                else -> return invalid
            }
        }
    }

    var integer: Int? = null
        get() {
            when (this) {
                reserved -> return 0
                checkedIn -> return 1
                absent -> return 2
                invalid -> return null
            }
        }
    var string: String? = null
        get() {
            when (this) {
                reserved -> return "예약완료"
                checkedIn -> return "출석"
                absent -> return "결석"
                invalid -> return null
            }
        }

}