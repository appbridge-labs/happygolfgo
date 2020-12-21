package com.artilearn.happygolfgo.ui.reservelist

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.reservelist.ReserveListResponse
import com.artilearn.happygolfgo.data.reservelist.source.ReserveListRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class ReserveListViewModel(
    private val reseveListRepository: ReserveListRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _reservationList: SingleLiveEvent<ReserveListResponse> = SingleLiveEvent()

    val reservationList: LiveData<ReserveListResponse>
        get() = _reservationList

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
                    _serverError.value = response.message()
                }
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
            }).addTo(compositeDisposable)
    }

}