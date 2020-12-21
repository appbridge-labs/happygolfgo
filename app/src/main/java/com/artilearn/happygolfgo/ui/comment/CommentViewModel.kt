package com.artilearn.happygolfgo.ui.comment

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.comment.CommentModel
import com.artilearn.happygolfgo.data.comment.source.CommentRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import com.artilearn.happygolfgo.util.ext.errorLog
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function4
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class CommentViewModel(
    private val commetRepository: CommentRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName
    private val _bottomClick = PublishSubject.create<Unit>()
    private val _ratingScore = BehaviorSubject.createDefault(1f)
    private val _comment = BehaviorSubject.createDefault("")
    private val _lessonId = BehaviorSubject.createDefault(0)

    private val _success: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _commentType: SingleLiveEvent<CommentEmptyType> = SingleLiveEvent()

    val success: LiveData<Unit>
        get() = _success

    val commentType: LiveData<CommentEmptyType>
        get() = _commentType

    init {
        bindRx()
    }

    fun bottomOnNext() {
        _bottomClick.onNext(Unit)
    }

    fun commentOnNext(comment: String) {
        _comment.onNext(comment)
    }

    fun ratingOnNext(rating: Float) {
        _ratingScore.onNext(rating)
    }

    fun lessonIdOnNext(lessonId: Int) {
        _lessonId.onNext(lessonId)
    }

    private fun bindRx() {
        _lessonId.subscribe().addTo(compositeDisposable)

        _bottomClick.withLatestFrom(
            _lessonId, _comment, _ratingScore,
            Function4 { _: Unit, lessonId: Int, comment: String, rating: Float ->
                Triple(lessonId, comment, rating)
            }
        )
            .map { val (i, c, r) = it; Triple(i, c, r) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { (id, comment, rating) ->
                when {
                    comment.isEmpty() -> _commentType.value = CommentEmptyType.EMPTY_COMMENT
                    rating == 0f -> _commentType.value = CommentEmptyType.EMPTY_RATING
                    else -> putDataModel(id, comment, rating.toInt())
                }
            }
            .addTo(compositeDisposable)
    }

    private fun putDataModel(lessonId: Int, comment: String, rating: Int) {
        val model = CommentModel(lessonLogID = lessonId, comment = comment, ratings = rating)
        val dataModel = hashMapOf<String, CommentModel>()
        dataModel["Data"] = model

        uploadComment(dataModel)
    }

    private fun uploadComment(model: HashMap<String, CommentModel>) {
        commetRepository.uploadLessonCommet(model)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .doOnError { t -> errorLog(TAG, t) }
            .subscribe({
                _success.call()
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

    enum class CommentEmptyType {
        EMPTY_RATING,
        EMPTY_COMMENT
    }

}