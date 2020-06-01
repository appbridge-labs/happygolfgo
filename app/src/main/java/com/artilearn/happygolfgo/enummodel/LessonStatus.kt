package com.artilearn.happygolfgo.enummodel

enum class LessonStatus {
    unusable,
    usable,
    invalid;

    companion object CREATOR {
        fun create(integer: Int): LessonStatus {
            when (integer) {
                0 -> return unusable
                1 -> return usable
                else -> return invalid
            }
        }
    }

    var integer: Int? = null
        get() {
            when (this) {
                unusable -> return 0
                usable -> return 1
                invalid -> return null
            }
        }
    var string: String? = null
        get() {
            when (this) {
                unusable -> return "예약중"
                usable -> return "예약가능"
                invalid -> return null
            }
        }

}