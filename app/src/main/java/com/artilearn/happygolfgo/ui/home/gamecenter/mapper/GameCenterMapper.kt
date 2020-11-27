package com.artilearn.happygolfgo.ui.home.gamecenter.mapper

interface GameCenterMapper<T, R> {
    fun mapper(from: T): R
}