package com.artilearn.happygolfgo.ui.gamerank.mapper

interface GolfRankMapperModel<T, M> {
    fun localMapper(from: T): M
    fun globalMapper(from: T): M
}