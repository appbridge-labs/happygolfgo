package com.artilearn.happygolfgo.ui.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.comment.CommentModel
import com.artilearn.happygolfgo.data.comment.source.CommentRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CommentViewModel(
    private val commetRepository: CommentRepository
) : BaseViewModel() {

    val rating = MutableLiveData(1f)
    var comment = MutableLiveData("")
    var lessonId: Int? = null

    private val _emptyRating: MutableLiveData<Unit> = MutableLiveData()
    private val _emptyComment: MutableLiveData<Unit> = MutableLiveData()
    private val _success: SingleLiveEvent<Unit> = SingleLiveEvent()

    val emptyRating: LiveData<Unit> get() = _emptyRating
    val emptyComment: LiveData<Unit> get() = _emptyComment
    val success: LiveData<Unit> get() = _success

    private var currentRating: Float? = null
    private var currentComment: String? = null

    fun uploadLessonCommet() {
        lessonId?.let { lessonId ->
            currentRating = rating.value
            currentComment = comment.value.toString()
            when {
                currentRating == 0f -> {
                    _emptyRating.value = Unit
                }
                currentComment.isNullOrEmpty() -> {
                    _emptyComment.value = Unit
                }
                else -> {
                    val dataModel =
                        CommentModel(lessonId, currentComment!!, currentRating!!.toInt())
                    val hashMap = hashMapOf<String, CommentModel>()
                    hashMap["Data"] = dataModel
                    compositeDisposable.add(
                        commetRepository.uploadLessonCommet(hashMap)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe { showLoading() }
                            .doAfterTerminate { hideLoading() }
                            .subscribe({
                                _success.value = Unit
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
            }
        }
    }

}