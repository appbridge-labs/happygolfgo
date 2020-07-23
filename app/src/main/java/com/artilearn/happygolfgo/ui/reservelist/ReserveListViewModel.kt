package com.artilearn.happygolfgo.ui.reservelist

import android.util.Log
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.reservelist.source.ReserveListRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ReserveListViewModel(
    private val reseveListRepository: ReserveListRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

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

                    } else {

                    }
                }, { t ->
                    Log.d(TAG, "fail: ${t.message}")
                    when (t.message) {
                        "Network Error" -> {

                        }
                        else -> {

                        }
                    }
                })
        )
    }
}