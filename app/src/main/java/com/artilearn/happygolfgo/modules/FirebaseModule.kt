package com.artilearn.happygolfgo.modules

import com.google.firebase.iid.FirebaseInstanceId
import org.koin.dsl.module

val firebaseModule = module {

    single { FirebaseInstanceId.getInstance() }
}