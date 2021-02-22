package com.artilearn.happygolfgo.ui.home.mypage.profilechange

import androidx.lifecycle.LiveData
import com.artilearn.happygolfgo.base.BaseViewModel
import com.artilearn.happygolfgo.data.profilechange.source.ProfileChangeRepository
import com.artilearn.happygolfgo.util.SingleLiveEvent

class ProfileChangeViewModel(
    repository: ProfileChangeRepository
) : BaseViewModel() {

    private val _profileImage: SingleLiveEvent<String?> = SingleLiveEvent()
    val profileImage: LiveData<String?>
        get() = _profileImage

    init {
        _profileImage.value = repository.profileImage
    }
}