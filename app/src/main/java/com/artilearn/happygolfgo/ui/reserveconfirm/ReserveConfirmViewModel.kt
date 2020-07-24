package com.artilearn.happygolfgo.ui.reserveconfirm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.ReservationLesson
import com.artilearn.happygolfgo.data.ReservationPlate
import com.artilearn.happygolfgo.data.reserveconfirm.ReserveLessonModel
import com.artilearn.happygolfgo.data.reserveconfirm.ReservePlateModel
import com.artilearn.happygolfgo.data.reserveconfirm.source.CancelRepository
import com.artilearn.happygolfgo.data.reserveconfirm.source.ReservationRepository
import com.artilearn.happygolfgo.data.reserveconfirm.source.ReserveConfirmRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ReserveConfirmViewModel(
    private val reserveConfirmRepository: ReserveConfirmRepository,
    private val reservationRepository: ReservationRepository,
    private val cancelRepository: CancelRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val _policy: MutableLiveData<String> = MutableLiveData()
    private val _plateModel: MutableLiveData<ReservationPlate> = MutableLiveData()
    private val _lessonModel: MutableLiveData<ReservationLesson> = MutableLiveData()
    private val _unKnowError: MutableLiveData<Unit> = MutableLiveData()
    private val _cancelSuccess: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _cancelFail: MutableLiveData<Unit> = MutableLiveData()

    val policy: LiveData<String> get() = _policy
    val plateModel: LiveData<ReservationPlate> get() = _plateModel
    val lessonModel: LiveData<ReservationLesson> get() = _lessonModel
    val unKnowError: LiveData<Unit> get() = _unKnowError
    val cancelSuccess: LiveData<Unit> get() = _cancelSuccess
    val cancelFail: LiveData<Unit> get() = _cancelFail

    var ticketId: Int? = null
    var plateAvailabilityId: Int? = null
    var lessonAvailabilityId: Int? = null
    
    fun requestPolicy() {
        compositeDisposable.add(
            reserveConfirmRepository.requestPolicy()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({ response ->
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        _policy.value = body.data.policy.body
                    } else {
                        _serverError.value = response.errorBody().toString()
                    }
                }, { t ->
                    Log.d(TAG, "fail: ${t.message}")
                    when (t.message) {
                        "Network Error" -> {
                            _networkFail.value = Unit
                        }
                        else -> {
                            _serverError.value = t.message
                        }
                    }
                })
        )
    }

    fun reservationPlate() {
        if (ticketId != null && plateAvailabilityId != null) {
            val dataModel = ReservePlateModel(ticketId!!, plateAvailabilityId!!)
            val data = hashMapOf<String, ReservePlateModel>()
            data["Data"] = dataModel
            compositeDisposable.add(
                reservationRepository.reservationPlate(data)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showLoading() }
                    .doAfterTerminate { hideLoading() }
                    .subscribe({ response ->
                        val body = response.body()
                        if (response.isSuccessful && body != null) {
                            _plateModel.value = body.data.plateReservation
                        } else {
                            _serverError.value = response.errorBody().toString()
                        }
                    }, { t ->
                        Log.d(TAG, "reservationPlate: ${t.message}")
                        when (t.message) {
                            "Network Error" -> {
                                _networkFail.value = Unit
                            }
                            else -> {
                                _serverError.value = t.message
                            }
                        }
                    })
            )
        } else {
            _unKnowError.value = Unit
        }
    }

    fun reservationLesson() {
        if (ticketId != null && lessonAvailabilityId != null) {
            val dataModel = ReserveLessonModel(ticketId!!, lessonAvailabilityId!!)
            val data = hashMapOf<String, ReserveLessonModel>()
            data["Data"] = dataModel
            compositeDisposable.add(
                reservationRepository.reservationLesson(data)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showLoading() }
                    .doAfterTerminate { hideLoading() }
                    .subscribe({ response ->
                        val body = response.body()
                        if (response.isSuccessful && body != null) {
                            _lessonModel.value = body.data.lessonReservaion
                        } else {
                            _serverError.value = response.errorBody().toString()
                        }
                    }, { t ->
                        Log.d(TAG, "reservationLesson: ${t.message}")
                        when (t.message) {
                            "Network Error" -> {
                                _networkFail.value = Unit
                            }
                            else -> {
                                _serverError.value = t.message
                            }
                        }
                    })
            )
        } else {
            _unKnowError.value = Unit
        }
    }

    fun reservationPlateCancel(plateId: Int) {
        compositeDisposable.add(
            cancelRepository.reservationPlateCancel(plateId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({
                    _cancelSuccess.value = Unit
                }, { t ->
                    Log.d(TAG, "reservationPlateCancel: ${t.message}")
                    when (t.message) {
                        "Network Error" -> {
                            _networkFail.value = Unit
                        }
                        else -> {
                            _cancelFail.value = Unit
                        }
                    }
                })
        )
    }

    fun reservationLessonCancel(lessonId: Int) {
        compositeDisposable.add(
            cancelRepository.reservationLessonCancel(lessonId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({
                    _cancelSuccess.value = Unit
                }, { t ->
                    Log.d(TAG, "reservationLessonCancel: ${t.message}")
                    when (t.message) {
                        "Network Error" -> {
                            _networkFail.value = Unit
                        }
                        else -> {
                            _cancelFail.value = Unit
                        }
                    }
                })
        )
    }
}