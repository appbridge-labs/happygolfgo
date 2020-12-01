package com.artilearn.happygolfgo.ui.golfrank.mapper

interface GolfRankMapperModel<T, M> {
    fun localMapper(from: T): M
    fun globalMapper(from: T): M
}