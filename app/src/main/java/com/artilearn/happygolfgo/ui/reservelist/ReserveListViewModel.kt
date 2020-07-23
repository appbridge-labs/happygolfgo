package com.artilearn.happygolfgo.ui.reservelist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.ReserveList
import com.artilearn.happygolfgo.data.reservelist.ReserveListResponse
import com.artilearn.happygolfgo.data.reservelist.source.ReserveListRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ReserveListViewModel(
    private val reseveListRepository: ReserveListRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    private val _reservationList: MutableLiveData<ReserveListResponse> = MutableLiveData()
    val reservationList: LiveData<ReserveListResponse> get() = _reservationList

    init {
        compositeDisposable.add(
            reseveListRepository.selectReservationList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({ response ->
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        _reservationList.value = body
                    } else {
                        // error
                    }
                }, { t ->
                    Log.d(TAG, "fail: ${t.message}")
                    when (t.message) {
                        "Network Error" -> {
                            // error
                        }
                        else -> {
                            // error
                        }
                    }
                })
        )
    }


}