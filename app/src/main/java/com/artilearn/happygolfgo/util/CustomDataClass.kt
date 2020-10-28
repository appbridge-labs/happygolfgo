package com.artilearn.happygolfgo.util

import java.io.Serializable

data class Quadruple<out A, out B, out C, out D>(
    val one: A,
    val two: B,
    val three: C,
    val four: D
) : Serializable {

    override fun toString(): String = "($one, $two, $three, $four)"
}

data class Qnintuple<out A, out B, out C, out D, out E>(
    val one: A,
    val two: B,
    val three: C,
    val four: D,
    val five: E
) : Serializable {

    override fun toString(): String = "($one, $two, $three, $four, $five)"
}

fun <T> Quadruple<T, T, T, T>.toList(): List<T> = listOf(one, two, three, four)
fun <T> Qnintuple<T, T, T, T, T>.toList(): List<T> = listOf(one, two, three, four, five)