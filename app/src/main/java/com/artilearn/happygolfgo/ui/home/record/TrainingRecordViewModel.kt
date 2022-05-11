package com.artilearn.happygolfgo.ui.home.record

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.gamecenter.source.GameCenterRepository
import com.artilearn.happygolfgo.ui.home.gamecenter.model.GCRankModel
import com.artilearn.happygolfgo.ui.home.record.mapper.*
import com.artilearn.happygolfgo.ui.home.record.model.*
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class TrainingRecordViewModel(
    private val repository: GameCenterRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _summaryTime: MutableLiveData<TRSummaryTimeModel> = MutableLiveData()
    private val _summaryWeight: MutableLiveData<TRSummaryWeightModel> = MutableLiveData()
    private val _summaryGoldPower: MutableLiveData<TRSummaryGolfPowerModel> = MutableLiveData()
    private val _golfPowerRecords: MutableLiveData<List<TRSummaryGolfPowerRecordModel>> = MutableLiveData()
    private val _summaryRound: MutableLiveData<TRSummaryRoundModel> = MutableLiveData()
    private val _roundRecords: MutableLiveData<List<TRSummaryRoundRecordModel>> = MutableLiveData()

    val summaryTime: LiveData<TRSummaryTimeModel>
        get() = _summaryTime

    val summaryWeight: LiveData<TRSummaryWeightModel>
        get() = _summaryWeight

    val summaryGolfPower: LiveData<TRSummaryGolfPowerModel>
        get() = _summaryGoldPower

    val golfPowerRecords:LiveData<List<TRSummaryGolfPowerRecordModel>>
        get() = _golfPowerRecords

    val summaryRound:LiveData<TRSummaryRoundModel>
        get() = _summaryRound

    val roundRecords:LiveData<List<TRSummaryRoundRecordModel>>
        get() = _roundRecords

    init {
        requestSummaryTime()
        requestSummaryWeight()
        requestSummaryGoldPower()
        requestSummaryRound()
    }

    private fun requestSummaryTime() {
        repository.getTrainingManagementSummaryTime()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
//            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode == 0) {
                        _summaryTime.value = TRSummaryTimeMapper.mapper(body.data.res);
                    } else {
                        _serverError.value = "데이타를 불러오는데 실패했습니다."
                    }
                } else {
                    _serverError.value = "데이타를 불러오는데 실패했습니다."
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    private fun requestSummaryWeight() {
        repository.getTrainingManagementSummaryWeight()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { showLoading() }
//            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode == 0) {
                        _summaryWeight.value = TRSummaryWeightMapper.mapper(body.data.res);
                    } else {
                        if (body.data.errorMessage.isNotEmpty()) {
                            _serverError.value = body.data.errorMessage;
                        }
                    }
                } else {
                    _serverError.value = "데이타를 불러오는데 실패했습니다."
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    private fun requestSummaryGoldPower() {
        repository.getTrainingManagementSummaryGolfPower()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode == 0) {
                        _summaryGoldPower.value = TRSummaryGolfPowerMapper.mapper(body.data.res);
                        _golfPowerRecords.value = TrainingManagementGolfPowerRecordsMapper.mapper(body.data.res);
                    } else {
                        if (body.data.errorMessage.isNotEmpty()) {
                            _serverError.value = body.data.errorMessage
                        }
                    }
                } else {
                    _serverError.value = "데이타를 불러오는데 실패했습니다."
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    private fun requestSummaryRound() {
        repository.getTrainingManagementSummaryRound()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({ response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.data.errorCode == 0) {
                        _summaryRound.value = TRSummaryRoundMapper.mapper(body.data.res);
//                        _golfPowerRecords.value = TrainingManagementGolfPowerRecordsMapper.mapper(body.data.res);
                    } else {
                        _serverError.value = "데이타를 불러오는데 실패했습니다."
                    }
                } else {
                    _serverError.value = "데이타를 불러오는데 실패했습니다."
                }
            }, { t ->
                when (t) {
                    is NetworkErrorException -> _networkFail.value = Unit
                    is HttpException -> {
                        if (t.code() == 419) _appRefresh.call()
                        else _serverError.value = "데이터를 불러오는데 실패했습니다"
                    }
                    else -> _serverError.value = "알 수 없는 오류 발생"
                }
            }).addTo(compositeDisposable)
    }

    fun onRankDetail() {

    }

    fun onGameDetail() {

    }

    enum class ButtonType {
        RANK,
        GAME
    }
}