package com.artilearn.happygolfgo.ui.home.mypage.profilechange

import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.profilechange.source.ProfileChangeRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

class ProfileChangeViewModel(
    repository: ProfileChangeRepository
) : BaseViewModel() {

    private val _gallerySubject: Subject<Unit> = PublishSubject.create()
    private val _defaultImageSubject: Subject<Unit> = PublishSubject.create()
    private val _cancelSubject: Subject<Unit> = PublishSubject.create()
    private val _buttonTypeSubject: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    private val _profileImage: SingleLiveEvent<String?> = SingleLiveEvent()
    val profileImage: LiveData<String?>
        get() = _profileImage

    private val _state: SingleLiveEvent<ProfileChangeState> = SingleLiveEvent()
    val state: LiveData<ProfileChangeState>
        get() = _state

    init {
        _profileImage.value = repository.profileImage

        compositeDisposable.addAll(
            _gallerySubject.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    _state.value = ProfileChangeState.GALLERY
                    _buttonTypeSubject.onNext(true)
                },
            _defaultImageSubject.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    _state.value = ProfileChangeState.DEFAULT_IMAGE
                    _buttonTypeSubject.onNext(false)
                },
            _cancelSubject.throttleFirst(1, TimeUnit.SECONDS)
                .withLatestFrom(_buttonTypeSubject, BiFunction { _: Unit, x: Boolean -> x })
                .subscribe {
                    if (it) {
                        _state.value = ProfileChangeState.UPDATE
                    } else {
                        _state.value = ProfileChangeState.BACK
                    }
                }
        )
    }

    fun onGalleryClick() = _gallerySubject.onNext(Unit)
    fun onDefaultImageClick() = _defaultImageSubject.onNext(Unit)
    fun onButtonClick() = _cancelSubject.onNext(Unit)
    fun setProfileImage(image: String?) {
        _profileImage.value = image
    }

    private fun uploadProfileImage() {
        _state.value = ProfileChangeState.UPDATE
        _state.value = ProfileChangeState.SUCCESS
        _state.value = ProfileChangeState.FAIL
    }

    enum class ProfileChangeState {
        BACK,
        UPDATE,
        SUCCESS,
        FAIL,
        GALLERY,
        DEFAULT_IMAGE
    }
}