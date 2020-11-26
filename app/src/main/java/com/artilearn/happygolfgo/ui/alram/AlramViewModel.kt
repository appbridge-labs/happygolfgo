package com.artilearn.happygolfgo.ui.alram

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.Alram
import com.artilearn.happygolfgo.data.ApiError
import com.artilearn.happygolfgo.data.alram.source.AlramRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class AlramViewModel(
    private val alramRepository: AlramRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _emptyAlramMessage: MutableLiveData<Boolean> = MutableLiveData()
    private val _alramMessages: MutableLiveData<ArrayList<Alram>> = MutableLiveData()

    val emptyAlramMessage: LiveData<Boolean> get() = _emptyAlramMessage
    val alramMessages: LiveData<ArrayList<Alram>> get() = _alramMessages

    init {
        compositeDisposable.add(
            alramRepository.getAlramMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .doOnError { t ->
                    try {
                        throw RuntimeException(TAG)
                    } catch (e: Exception) {
                        FirebaseCrashlytics.getInstance().log("$t\n${t.localizedMessage}")
                    }
                }
                .subscribe({
                    val body = it.body()
                    if (it.isSuccessful && body != null) {
                        if (body.data.notifications.isNotEmpty()) {
                            hideEmptyAlramMessage()
                            _alramMessages.value = body.data.notifications as ArrayList<Alram>
                        } else {
                            showEmptyAlramMessage()
                        }
                    } else {
                        _serverError.value = it.message()
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
                })
        )
    }

    private fun showEmptyAlramMessage() {
        _emptyAlramMessage.value = true
    }

    private fun hideEmptyAlramMessage() {
        _emptyAlramMessage.value = false
    }
}