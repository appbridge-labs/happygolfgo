package com.artilearn.happygolfgo.ui.home.record

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.gamecenter.source.GameCenterRepository
import com.artilearn.happygolfgo.ui.home.record.model.RankingBySubjectData
import com.artilearn.happygolfgo.util.ext.errorLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class GolfRankingBySubjectViewModel (
    private val repository: GameCenterRepository
): BaseViewModel() {
    private val _classNameForLog = javaClass.simpleName

    private var disposable : Disposable? = null
    var pageIndex:Int? = null
    private val _rankingBySubjectData:MutableLiveData<ArrayList<RankingBySubjectData>>
      = MutableLiveData()

    val rankingBySubjectData : LiveData<ArrayList<RankingBySubjectData>>
    get() = _rankingBySubjectData

    var title :String = ""

    init {

    }

    fun request() {
        requestRankingSubject()
    }

    private fun requestRankingSubject() {
        disposable?.let {
             if(!it.isDisposed)  {
                it.dispose()
             }
        }
        pageIndex?.let {
              disposable = repository.getSubjectRanking(it)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .doOnSubscribe {
                      showLoading()
                  }
                  .doAfterTerminate {
                      hideLoading()
                  }
                  .doOnError { error ->
                      errorLog(_classNameForLog, error)
                  }
                  .subscribe({ response ->
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                         val rankingBySubjectData = RankingBySubjectData.map(body.data.res)
                          _rankingBySubjectData.value = rankingBySubjectData
                         if (body.data.res.pageInfo.title != null ) {
                             title = body.data.res.pageInfo.title ?: ""
                         }
                    } else {
                        _serverError.value = CONSTANT_ERROR_MESSAGE_FAILED_TO_LOAD_DATA_FROM_REMOTE
                    }
                  }, {error ->
                      when(error) {
                           is NetworkErrorException -> _networkFail.value = Unit
                          else -> _serverError.value = CONSTANT_ERROR_MESSAGE_UNKNOWN_ERROR
                      }
                  })
                  .addTo(compositeDisposable)
        }
    }
}