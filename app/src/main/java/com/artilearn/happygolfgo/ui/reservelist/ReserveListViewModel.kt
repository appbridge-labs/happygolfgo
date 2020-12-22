package com.artilearn.happygolfgo.ui.reservelist

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.reservelist.ReserveListResponse
import com.artilearn.happygolfgo.data.reservelist.source.ReserveListRepository
<<<<<<< HEAD
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
=======
>>>>>>> hot-test-3
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class ReserveListViewModel(
    private val reseveListRepository: ReserveListRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
<<<<<<< HEAD
    private val _reservationList: SingleLiveEvent<ReserveListResponse> = SingleLiveEvent()

    val reservationList: LiveData<ReserveListResponse>
        get() = _reservationList
=======
    private val _reservationList: MutableLiveData<ReserveListResponse> = MutableLiveData()
    val reservationList: LiveData<ReserveListResponse> get() = _reservationList
>>>>>>> hot-test-3

    init {
        reseveListRepository.selectReservationList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    _reservationList.value = body
                } else {
                    _serverError.value = "예약 정보를 불러오는데 실패했습니다"
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
                    }
<<<<<<< HEAD
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
=======
                }, { t ->
                    when (t) {
                        is NetworkErrorException -> _networkFail.value = Unit
                        is HttpException -> {
                            try {
                                throw RuntimeException(TAG)
                            } catch (e: Exception) {
                                FirebaseCrashlytics.getInstance().log("$t\n${t.localizedMessage}\n${t.code()}\n${t.message()}")
                            }
                            _serverError.value = t.localizedMessage
                        }
                        else -> _serverError.value = t.localizedMessage
                    }
                })
        )
>>>>>>> hot-test-3
    }

}