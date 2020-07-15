package com.artilearn.happygolfgo.data.pwauth

data class SendAuthModel(
    val phoneNumber: String,
    val clientType: Int = 0
)