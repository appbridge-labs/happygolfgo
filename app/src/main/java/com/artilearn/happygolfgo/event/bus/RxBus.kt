package com.artilearn.happygolfgo.event.bus

import android.content.Intent
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

object RxBus {

    private val _fcmSubject: Subject<Intent> = BehaviorSubject.create()
    val fcmSubject = _fcmSubject.hide()

    private val _aaa: Subject<Unit> = PublishSubject.create()
    val aaa = _aaa.hide()

    fun notifyFcmMessage(intent: Intent) {
        _fcmSubject.onNext(intent)
    }

    fun aaaa() {
        _aaa.onNext(Unit)
    }
}