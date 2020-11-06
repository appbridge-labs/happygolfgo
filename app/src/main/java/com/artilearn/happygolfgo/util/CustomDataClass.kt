package com.artilearn.happygolfgo.util

import com.github.mikephil.charting.formatter.ValueFormatter
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

class LabelCustomFormatter : ValueFormatter() {
    private var index = 0

    override fun getFormattedValue(value: Float): String {
        index = value.toInt()
        return when (index) {
            6 -> "드라이버"
            7 -> "아이언"
            8 -> "우드"
            9 -> "어프로치"
            10 -> "퍼팅"
            else -> throw IndexOutOfBoundsException("index out")
        }
    }
}

class ScoreCustomFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val score = value.toString().split(".")
        return score[0]+"점"
    }
}