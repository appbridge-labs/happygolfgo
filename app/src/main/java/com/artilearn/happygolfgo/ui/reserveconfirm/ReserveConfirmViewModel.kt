package com.artilearn.happygolfgo.ui.reserveconfirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.ReservationLesson
import com.artilearn.happygolfgo.data.ReservationPlate
import com.artilearn.happygolfgo.data.reserveconfirm.LessonResponse
import com.artilearn.happygolfgo.data.reserveconfirm.PlateResponse
import com.artilearn.happygolfgo.data.reserveconfirm.ReserveLessonModel
import com.artilearn.happygolfgo.data.reserveconfirm.ReservePlateModel
import com.artilearn.happygolfgo.data.reserveconfirm.source.CancelRepository
import com.artilearn.happygolfgo.data.reserveconfirm.source.ReservationRepository
import com.artilearn.happygolfgo.data.reserveconfirm.source.ReserveConfirmRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class ReserveConfirmViewModel(
    private val reserveConfirmRepository: ReserveConfirmRepository,
    private val reservationRepository: ReservationRepository,
    private val cancelRepository: CancelRepository
) : BaseViewModel() {

    private val _status: MutableLiveData<ReservationStatus> = MutableLiveData()
    private val _plateModel: MutableLiveData<PlateResponse> = MutableLiveData()
    private val _lessonModel: MutableLiveData<LessonResponse> = MutableLiveData()
    private val _policy: MutableLiveData<String> = MutableLiveData()
    private val _unKnowError: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _reservationPlate: MutableLiveData<ReservationPlate> = MutableLiveData()
    private val _reservationLesson: MutableLiveData<ReservationLesson> = MutableLiveData()

    val status: LiveData<ReservationStatus> get() = _status
    val plateModel: LiveData<PlateResponse> get() = _plateModel
    val lessonModel: LiveData<LessonResponse> get() = _lessonModel
    val policy: LiveData<String> get() = _policy
    val unKnowError: LiveData<Unit> get() = _unKnowError
    val reservationPlate: LiveData<ReservationPlate> get() = _reservationPlate
    val reservationLesson: LiveData<ReservationLesson> get() = _reservationLesson

    var plateAvailabilityId: Int? = null
    var lessonAvailabilityId: Int? = null
    var ticketId: Int? = null

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
                        _serverError.value = response.message()
                    }
                }, { t ->
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
                            _reservationPlate.value = body.data.plateReservation
                        } else {
                            _serverError.value = response.message()
                        }
                    }, { t ->
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
            _unKnowError.call()
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
                            _reservationLesson.value = body.data.lessonReservaion
                        } else {
                            _serverError.value = response.message()
                        }
                    }, { t ->
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
            _unKnowError.call()
        }
    }

    fun getReservationLessonDetail(lessonId: Int) {
        reserveConfirmRepository.getReservationLessonDetail(lessonId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .subscribe({ result ->
                val body = result.body()
                if (result.isSuccessful && body != null) {
                    _lessonModel.value = body.data
                } else {
                    _serverError.value = result.message()
                }
            }, { t ->
                when (t.message) {
                    "Network Error" -> {
                        _networkFail.value = Unit
                    }
                    else -> {
                        _serverError.value = t.message
                    }
                }
            }).addTo(compositeDisposable)
    }

    fun getReservationPlateDetail(plateId: Int) {
        reserveConfirmRepository.getReservationPlateDetail(plateId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .subscribe({ result ->
                val body = result.body()
                if (result.isSuccessful && body != null) {
                    _plateModel.value = body.data
                } else {
                    _serverError.value = result.message()
                }
            }, { t ->
                when (t.message) {
                    "Network Error" -> {
                        _networkFail.value = Unit
                    }
                    else -> {
                        _serverError.value = t.message
                    }
                }
            }).addTo(compositeDisposable)
    }

    fun reservationPlateCancel(plateId: Int) {
        compositeDisposable.add(
            cancelRepository.reservationPlateCancel(plateId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({
                    _status.value = ReservationStatus.CANCEL_SUCCESS
                }, { t ->
                    when (t.message) {
                        "Network Error" -> {
                            _networkFail.value = Unit
                        }
                        else -> {
                            _status.value = ReservationStatus.CANCEL_FAIL
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
                    _status.value = ReservationStatus.CANCEL_SUCCESS
                }, { t ->
                    when (t.message) {
                        "Network Error" -> {
                            _networkFail.value = Unit
                        }
                        else -> {
                            _status.value = ReservationStatus.CANCEL_FAIL
                        }
                    }
                })
        )
    }

    enum class ReservationStatus {
        CANCEL_SUCCESS,
        CANCEL_FAIL
    }
}