package com.artilearn.happygolfgo.enummodel

enum class Sex {
    male,
    female,
    invalid;

    companion object CREATOR {
        fun create(integer: Int): Sex {
            when (integer) {
                0 -> return male
                1 -> return female
                else -> return invalid
            }
        }
    }

    var integer: Int? = null
        get() {
            when (this) {
                male -> return 0
                female -> return 1
                invalid -> return null
            }
        }
    var string: String? = null
        get() {
            when (this) {
                male -> return "남"
                female -> return "여"
                invalid -> return "미선택"
            }
        }

}