package com.artilearn.happygolfgo.data.gamecenter.mapper

interface GameCenterMyInfoMapper<T, R> {
    fun mapper(from: T): R
}