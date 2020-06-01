package com.artilearn.happygolfgo.enummodel

enum class TicketType {
    privateLesson,
    groupLesson,
    plateUsage,
    place,
    locker,
    extra,
    invalid;

    companion object CREATOR {
        fun create(integer: Int): TicketType {
            when (integer) {
                0 -> return privateLesson
                1 -> return groupLesson
                2 -> return plateUsage
                3 -> return place
                4 -> return locker
                5 -> return extra
                else -> return invalid
            }
        }
    }

    var integer: Int? = null
        get() {
            when (this) {
                privateLesson -> return 0
                groupLesson -> return 1
                plateUsage -> return 2
                place -> return 3
                locker -> return 4
                extra -> return 5
                invalid -> return null
            }
        }
    var string: String? = null
        get() {
            when (this) {
                privateLesson -> return "privateLesson"
                groupLesson -> return "groupLesson"
                plateUsage -> return "plateUsage"
                place -> return "place"
                locker -> return "locker"
                extra -> return "extra"
                invalid -> return null
            }
        }

}